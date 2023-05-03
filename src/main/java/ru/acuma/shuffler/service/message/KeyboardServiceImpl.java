package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.constant.Command;
import ru.acuma.shuffler.model.constant.GameState;
import ru.acuma.shuffler.model.constant.keyboards.Created;
import ru.acuma.shuffler.model.constant.keyboards.Game;
import ru.acuma.shuffler.model.constant.keyboards.Playing;
import ru.acuma.shuffler.model.constant.keyboards.Ready;
import ru.acuma.shuffler.model.constant.keyboards.checking.Checking;
import ru.acuma.shuffler.model.constant.keyboards.checking.Checking1;
import ru.acuma.shuffler.model.constant.keyboards.checking.Checking2;
import ru.acuma.shuffler.model.constant.keyboards.checking.Checking3;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class KeyboardServiceImpl implements KeyboardService {

    @Override
    public InlineKeyboardMarkup getLobbyKeyboard(TEvent event) {
        var lobbyButtons = buildLobbyButtons(event);

        return buildKeyboard(lobbyButtons);
    }

    @Override
    public InlineKeyboardMarkup getCheckingKeyboard(TEvent event) {
        List<EventStatusButton> checkingButtons = List.of(Checking.values());

        return buildKeyboard(checkingButtons);
    }

    @Override
    public InlineKeyboardMarkup getGamingKeyboard(TEvent event) {
        var gameState = event.getCurrentGame().getStatus();
        var gameButtons = buildGameButtons(gameState);

        return buildKeyboard(gameButtons);
    }

    @Override
    public InlineKeyboardMarkup getEmptyKeyboard() {
        return InlineKeyboardMarkup.builder()
                .build();
    }

    @Override
    public InlineKeyboardMarkup getTimedKeyboard(int time) {
        var names = buildTimedButtons(time);
        return buildKeyboard(names);
    }

    @Override
    public InlineKeyboardMarkup getDynamicListKeyboard(Command command, Map<Long, String> map, InlineKeyboardButton... buttons) {
        var rows = map.entrySet().stream()
                .map(entry -> buildButtonWithParam(command, entry.getKey(), entry.getValue()))
                .map(this::wrapToRow)
                .collect(Collectors.toList());
        var extraButtons = Arrays.stream(buttons)
                .map(this::wrapToRow)
                .toList();
        rows.addAll(extraButtons);

        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }

    @Override
    public InlineKeyboardButton getSingleButton(Command command, String text) {
        return InlineKeyboardButton.builder()
                .callbackData(command.getCommand())
                .text(text)
                .build();
    }

    private List<InlineKeyboardButton> wrapToRow(InlineKeyboardButton button) {
        return List.of(button);
    }

    private InlineKeyboardButton buildButtonWithParam(Command command, Long identifier, String text) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(command.getCommand() + "?" + identifier)
                .build();
    }

    private InlineKeyboardMarkup buildKeyboard(List<EventStatusButton> buttons) {
        return buttons.isEmpty()
                ? new InlineKeyboardMarkup(Collections.emptyList())
                : buildKeyboardStructure(buttons);
    }

    private InlineKeyboardMarkup buildKeyboardStructure(List<EventStatusButton> names) {
        var buttons = IntStream.rangeClosed(1, 3)
                .mapToObj(rowNum -> getInlineKeyboardButtons(names, rowNum))
                .toList();

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    private List<InlineKeyboardButton> getInlineKeyboardButtons(List<EventStatusButton> names, int row) {
        return names.stream()
                .filter(button -> button.getRow() == row)
                .map(button -> InlineKeyboardButton.builder()
                        .text(button.getAlias())
                        .callbackData(button.getAction())
                        .build())
                .toList();
    }

    private List<EventStatusButton> buildTimedButtons(Integer time) {
        return switch (time) {
            case 3 -> List.of(Checking3.values());
            case 2 -> List.of(Checking2.values());
            case 1 -> List.of(Checking1.values());
            default -> List.of(Checking.values());
        };
    }

    private List<EventStatusButton> buildLobbyButtons(TEvent event) {
        var eventState = event.getEventStatus();

        return switch (eventState) {
            case CREATED -> List.of(Created.values());
            case READY -> List.of(Ready.values());
            case GAME_CHECKING, CANCEL_CHECKING, FINISH_CHECKING, BEGIN_CHECKING, EVICTING, ANY, CANCELLED, FINISHED ->
                    Collections.emptyList();
            case PLAYING, WAITING_WITH_GAME, WAITING -> List.of(Playing.values());
        };
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    private List<EventStatusButton> buildGameButtons(GameState gameState) {
        return switch (gameState) {
            case ACTIVE -> List.of(Game.values());
            default -> Collections.emptyList();
        };
    }

}
