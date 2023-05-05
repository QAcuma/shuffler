package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.acuma.shuffler.model.constant.Command;
import ru.acuma.shuffler.model.constant.GameStatus;
import ru.acuma.shuffler.model.constant.keyboards.Keyboards;
import ru.acuma.shuffler.model.constant.keyboards.ShufflerButton;
import ru.acuma.shuffler.model.domain.TEvent;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    public InlineKeyboardMarkup getLobbyKeyboard(TEvent event) {
        var lobbyButtons = buildLobbyButtons(event);

        return buildKeyboard(lobbyButtons);
    }

    public InlineKeyboardMarkup getCheckingKeyboard(final Integer timer) {
        var buttons = timer > BigDecimal.ZERO.intValue()
                      ? buildTimerButtons(timer)
                      : Keyboards.CHECKING_BUTTONS;

        return buildKeyboard(buttons);
    }

    public InlineKeyboardMarkup getCheckingKeyboard() {
        return getCheckingKeyboard(0);
    }

    public InlineKeyboardMarkup getWaitingKeyboard() {
        return buildKeyboard(Keyboards.IDLE_BUTTONS);
    }

    public InlineKeyboardMarkup getGamingKeyboard(TEvent event) {
        var gameState = event.getCurrentGame().getStatus();
        var gameButtons = buildGameButtons(gameState);

        return buildKeyboard(gameButtons);
    }

    public InlineKeyboardMarkup getEmptyKeyboard() {
        return InlineKeyboardMarkup.builder()
            .build();
    }

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

    private InlineKeyboardMarkup buildKeyboard(List<ShufflerButton> buttons) {
        return buttons.isEmpty()
               ? new InlineKeyboardMarkup(Collections.emptyList())
               : buildKeyboardStructure(buttons);
    }

    private InlineKeyboardMarkup buildKeyboardStructure(List<ShufflerButton> names) {
        var buttons = IntStream.rangeClosed(1, 3)
            .mapToObj(rowNum -> getInlineKeyboardButtons(names, rowNum))
            .toList();

        return InlineKeyboardMarkup.builder()
            .keyboard(buttons)
            .build();
    }

    private List<InlineKeyboardButton> getInlineKeyboardButtons(List<ShufflerButton> names, int row) {
        return names.stream()
            .filter(button -> button.getRow() == row)
            .map(button -> InlineKeyboardButton.builder()
                .text(button.getAlias())
                .callbackData(button.getAction())
                .build())
            .toList();
    }

    private List<ShufflerButton> buildTimerButtons(Integer time) {
        return switch (time) {
            case 3 -> Keyboards.IDLE_3_BUTTONS;
            case 2 -> Keyboards.IDLE_2_BUTTONS;
            case 1 -> Keyboards.IDLE_1_BUTTONS;
            default -> Keyboards.CHECKING_BUTTONS;
        };
    }

    private List<ShufflerButton> buildLobbyButtons(TEvent event) {
        var eventState = event.getEventStatus();

        return switch (eventState) {
            case CREATED -> Keyboards.LOBBY_BUTTONS;
            case READY -> Keyboards.LOBBY_READY_BUTTONS;
            case CANCEL_CHECKING, FINISH_CHECKING, BEGIN_CHECKING, EVICTING -> Keyboards.CHECKING_BUTTONS;
            case GAME_CHECKING, ANY, CANCELLED, FINISHED -> Collections.emptyList();
            case PLAYING, WAITING_WITH_GAME, WAITING -> Keyboards.LOBBY_PLAYING_BUTTONS;
        };
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    private List<ShufflerButton> buildGameButtons(final GameStatus gameStatus) {
        return switch (gameStatus) {
            case ACTIVE -> Keyboards.GAME_BUTTONS;
            case RED_CHECKING, BLUE_CHECKING, CANCEL_CHECKING -> Keyboards.CHECKING_BUTTONS;
            default -> Collections.emptyList();
        };
    }
}
