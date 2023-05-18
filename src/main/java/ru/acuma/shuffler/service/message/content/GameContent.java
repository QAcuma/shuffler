package ru.acuma.shuffler.service.message.content;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.context.Render;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.message.KeyboardService;
import ru.acuma.shuffler.service.message.MessageTextService;

@Service
@RequiredArgsConstructor
public class GameContent implements Fillable, WithText<TEvent>, WithKeyboard<TEvent> {

    private final EventContext eventContext;
    private final MessageTextService messageTextService;
    private final KeyboardService keyboardService;

    @Override
    public void fill(final Render render, final Long chatId) {
        var event = eventContext.findEvent(chatId);
        fillKeyboard(render, event);
        fillText(render, event);
    }

    @Override
    public void fillKeyboard(final Render render, final TEvent event) {
        var keyboard = keyboardService.getGamingKeyboard(event);
        render.withKeyboard(keyboard);
    }

    @Override
    public void fillText(final Render render, final TEvent event) {
        var text = messageTextService.buildGameContent(event);
        render.withText(text);
    }
}
