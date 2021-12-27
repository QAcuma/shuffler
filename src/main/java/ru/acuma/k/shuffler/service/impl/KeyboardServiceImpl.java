package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.acuma.k.shuffler.cache.EventHolder;
import ru.acuma.k.shuffler.model.enums.EventState;
import ru.acuma.k.shuffler.model.enums.keyboards.Checking;
import ru.acuma.k.shuffler.model.enums.keyboards.Created;
import ru.acuma.k.shuffler.model.enums.keyboards.Finished;
import ru.acuma.k.shuffler.model.enums.keyboards.Playing;
import ru.acuma.k.shuffler.model.enums.keyboards.Ready;
import ru.acuma.k.shuffler.service.KeyboardService;
import ru.acuma.k.shuffler.service.enums.EventStatusApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeyboardServiceImpl implements KeyboardService {

    private final EventHolder eventHolder;

    @Override
    public InlineKeyboardMarkup getKeyboard(Long groupId) {
        var event = eventHolder.getEvent(groupId);
        var names = getButtons(event.getEventState());
        List<InlineKeyboardButton> buttons = names.stream()
                .map(button -> InlineKeyboardButton.builder()
                        .text(button.getAlias())
                        .callbackData(button.getAction())
                        .build())
                .collect(Collectors.toList());

        return InlineKeyboardMarkup.builder()
                .keyboard(Collections.singletonList(buttons))
                .build();
    }

    private List<EventStatusApi> getButtons(EventState eventState) {
        switch (eventState) {
            case CREATED:
                return List.of(Created.values());
            case READY:
                return List.of(Ready.values());
            case CHECKING:
                return List.of(Checking.values());
            case PLAYING:
                return List.of(Playing.values());
            case FINISHED:
                return List.of(Finished.values());
            default:
                return new ArrayList<>();
        }
    }
}
