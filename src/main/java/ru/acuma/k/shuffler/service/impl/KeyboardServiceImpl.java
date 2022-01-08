package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.enums.EventState;
import ru.acuma.k.shuffler.model.enums.keyboards.Created;
import ru.acuma.k.shuffler.model.enums.keyboards.Finished;
import ru.acuma.k.shuffler.model.enums.keyboards.Game;
import ru.acuma.k.shuffler.model.enums.keyboards.Playing;
import ru.acuma.k.shuffler.model.enums.keyboards.Ready;
import ru.acuma.k.shuffler.model.enums.keyboards.checking.Checking;
import ru.acuma.k.shuffler.model.enums.keyboards.checking.Checking1;
import ru.acuma.k.shuffler.model.enums.keyboards.checking.Checking2;
import ru.acuma.k.shuffler.model.enums.keyboards.checking.Checking3;
import ru.acuma.k.shuffler.model.enums.keyboards.checking.Checking4;
import ru.acuma.k.shuffler.model.enums.keyboards.checking.Checking5;
import ru.acuma.k.shuffler.model.enums.messages.MessageType;
import ru.acuma.k.shuffler.service.KeyboardService;
import ru.acuma.k.shuffler.service.enums.EventStatusApi;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeyboardServiceImpl implements KeyboardService {

    @Override
    public InlineKeyboardMarkup getKeyboard(KickerEvent event, MessageType type) {
        EventState state = event.getEventState();
        switch (type) {
            case GAME:
                return buildKeyboard(buildGameButtons(state));
            case CHECKING:
            default:
                return buildKeyboard(buildButtons(state));
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

    private InlineKeyboardMarkup buildKeyboard(List<EventStatusApi> names) {
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

    public List<EventStatusApi> buildTimedButtons(int time) {
        switch (time) {
            case 5:
                return List.of(Checking5.values());
            case 4:
                return List.of(Checking4.values());
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

    private List<EventStatusApi> buildButtons(EventState eventState) {
        switch (eventState) {
            case CREATED:
                return List.of(Created.values());
            case READY:
                return List.of(Ready.values());
            case CANCEL_LOBBY_CHECKING:
            case BEGIN_CHECKING:
            case CANCEL_GAME_CHECKING:
            case RED_CHECKING:
            case BLUE_CHECKING:
            case MEMBER_CHECKING:
            case FINISH_CHECKING:
                return List.of(Checking.values());
            case PLAYING:
                return List.of(Playing.values());
            case FINISHED:
            default:
                return new ArrayList<>();
        }
    }

    private List<EventStatusApi> buildGameButtons(EventState eventState) {
        switch (eventState) {
            case PLAYING:
                return List.of(Game.values());
            case RED_CHECKING:
            case BLUE_CHECKING:
            case FINISH_CHECKING:
            case CANCEL_GAME_CHECKING:
            default:
                return new ArrayList<>();
        }
    }
}
