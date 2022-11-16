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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static ru.acuma.shuffler.model.enums.EventState.BEGIN_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.CANCEL_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.CREATED;
import static ru.acuma.shuffler.model.enums.EventState.FINISHED;
import static ru.acuma.shuffler.model.enums.EventState.FINISH_CHECKING;
import static ru.acuma.shuffler.model.enums.EventState.PLAYING;
import static ru.acuma.shuffler.model.enums.EventState.READY;
import static ru.acuma.shuffler.model.enums.EventState.WAITING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING_WITH_GAME;
import static ru.acuma.shuffler.model.enums.messages.MessageType.CANCELLED;
import static ru.acuma.shuffler.model.enums.messages.MessageType.CHECKING;
import static ru.acuma.shuffler.model.enums.messages.MessageType.CHECKING_TIMED;
import static ru.acuma.shuffler.model.enums.messages.MessageType.GAME;
import static ru.acuma.shuffler.model.enums.messages.MessageType.LOBBY;
import static ru.acuma.shuffler.model.enums.messages.MessageType.STAT;

@Service
@RequiredArgsConstructor
public class KeyboardServiceImpl implements KeyboardService {

    private final Map<EventState, Supplier<List<EventStatusButton>>> lobbyButtons = Map.of(
            CREATED, () -> List.of(Created.values()),
            READY, () -> List.of(Ready.values()),
            CANCEL_CHECKING, () -> List.of(Checking.values()),
            BEGIN_CHECKING, () -> List.of(Checking.values()),
            FINISH_CHECKING, () -> List.of(Checking.values()),
            PLAYING, () -> List.of(Playing.values()),
            WAITING_WITH_GAME, () -> List.of(Playing.values()),
            WAITING, () -> List.of(Playing.values()),
            FINISHED, Collections::emptyList
    );

    private final Map<MessageType, Function<TgEvent, InlineKeyboardMarkup>> messageButtons = Map.of(
            CHECKING, event -> buildKeyboard(List.of(Checking.values())),
            CHECKING_TIMED, event -> buildKeyboard(buildLobbyButtons(event)),
            CANCELLED, event -> buildKeyboard(buildLobbyButtons(event)),
            GAME, event -> buildKeyboard(buildLobbyButtons(event)),
            LOBBY, event -> buildKeyboard(buildLobbyButtons(event)),
            STAT, event -> buildKeyboard(buildLobbyButtons(event))
    );

    private final Map<Integer, Supplier<List<EventStatusButton>>> timedButtons = Map.of(
            3, () -> List.of(Checking3.values()),
            2, () -> List.of(Checking2.values()),
            1, () -> List.of(Checking1.values()),
            0, () -> List.of(Checking.values())
    );

    @Override
    public InlineKeyboardMarkup getKeyboard(TgEvent event, MessageType type) {
        return messageButtons.get(type).apply(event);
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

    private List<EventStatusButton> buildTimedButtons(Integer time) {
        return timedButtons.getOrDefault(time, () -> List.of(Checking.values())).get();
    }

    private List<EventStatusButton> buildLobbyButtons(TgEvent event) {
        var eventState = event.getEventState();
        var gameState = event.getLatestGameState();
        var buttons = lobbyButtons.get(eventState).get();
        var checkingButtons = buttons.stream()
                .filter(button -> button.getRow() == 1)
                .collect(Collectors.toList());

        return gameState.in(GameState.RED_CHECKING, GameState.BLUE_CHECKING, GameState.CANCEL_CHECKING)
                ? checkingButtons
                : buttons;
    }

    private List<EventStatusButton> buildGameButtons(TgEvent event) {
        return event.getLatestGameState() == GameState.ACTIVE && event.getEventState() != FINISH_CHECKING
                ? List.of(Game.values())
                : new ArrayList<>();
    }

}
