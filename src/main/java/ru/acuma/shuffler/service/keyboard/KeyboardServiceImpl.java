package ru.acuma.shuffler.service.keyboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.model.enums.GameState;
import ru.acuma.shuffler.model.enums.keyboards.Created;
import ru.acuma.shuffler.model.enums.keyboards.Game;
import ru.acuma.shuffler.model.enums.keyboards.GameChecking;
import ru.acuma.shuffler.model.enums.keyboards.Playing;
import ru.acuma.shuffler.model.enums.keyboards.Ready;
import ru.acuma.shuffler.model.enums.keyboards.checking.Checking;
import ru.acuma.shuffler.model.enums.keyboards.checking.Checking1;
import ru.acuma.shuffler.model.enums.keyboards.checking.Checking2;
import ru.acuma.shuffler.model.enums.keyboards.checking.Checking3;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.KeyboardService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeyboardServiceImpl implements KeyboardService {

    @Override
    public InlineKeyboardMarkup getKeyboard(TgEvent event, MessageType type) {
        switch (type) {
            case GAME:
                return buildKeyboard(buildGameButtons(event));
            case CHECKING:
                return buildKeyboard(List.of(Checking.values()));
            default:
                return buildKeyboard(buildButtons(event));
        }
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
                .collect(Collectors.toList());
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

    private InlineKeyboardMarkup buildKeyboard(List<EventStatusButton> names) {
        List<List<InlineKeyboardButton>> buttons = new LinkedList<>();

        List<InlineKeyboardButton> row1 = names.stream()
                .filter(button -> button.getRow() == 1)
                .map(button -> InlineKeyboardButton.builder()
                        .text(button.getAlias())
                        .callbackData(button.getAction())
                        .build())
                .collect(Collectors.toList());

        List<InlineKeyboardButton> row2 = names.stream()
                .filter(button -> button.getRow() == 2)
                .map(button -> InlineKeyboardButton.builder()
                        .text(button.getAlias())
                        .callbackData(button.getAction())
                        .build())
                .collect(Collectors.toList());

        List<InlineKeyboardButton> row3 = names.stream()
                .filter(button -> button.getRow() == 3)
                .map(button -> InlineKeyboardButton.builder()
                        .text(button.getAlias())
                        .callbackData(button.getAction())
                        .build())
                .collect(Collectors.toList());

        buttons.add(row1);
        buttons.add(row2);
        buttons.add(row3);

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    private List<EventStatusButton> buildTimedButtons(int time) {
        switch (time) {
            case 3:
                return List.of(Checking3.values());
            case 2:
                return List.of(Checking2.values());
            case 1:
                return List.of(Checking1.values());
            default:
                return List.of(Checking.values());
        }
    }

    private List<EventStatusButton> buildButtons(TgEvent event) {
        var eventState = event.getEventState();
        var gameState = event.getLatestGameState();

        switch (eventState) {
            case CREATED:
                return List.of(Created.values());
            case READY:
                return List.of(Ready.values());
            case CANCEL_CHECKING:
            case BEGIN_CHECKING:
            case FINISH_CHECKING:
                return List.of(Checking.values());
            case PLAYING:
                return gameState.in(GameState.RED_CHECKING, GameState.BLUE_CHECKING, GameState.CANCEL_CHECKING)
                        ? List.of(GameChecking.values())
                        : List.of(Playing.values());
            case WAITING:
            case WAITING_WITH_GAME:
                return List.of(Playing.values());
            case FINISHED:
            default:
                return new ArrayList<>();
        }
    }

    private List<EventStatusButton> buildGameButtons(TgEvent event) {
        return event.getLatestGameState() == GameState.ACTIVE && event.getEventState() != EventState.FINISH_CHECKING
                ? List.of(Game.values())
                : new ArrayList<>();
    }

}
