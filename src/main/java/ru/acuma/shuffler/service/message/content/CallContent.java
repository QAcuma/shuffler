package ru.acuma.shuffler.service.message.content;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.context.cotainer.Render;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.service.message.KeyboardService;
import ru.acuma.shuffler.service.message.MessageTextService;

@Service
@RequiredArgsConstructor
public class CallContent implements Fillable, WithText<TGame> {

    private final EventContext eventContext;
    private final MessageTextService messageTextService;

    @Override
    public void fill(final Render render, final Long chatId) {
        var event = eventContext.findEvent(chatId);
        fillText(render, event.getCurrentGame());
    }

    @Override
    public void fillText(final Render render, final TGame game) {
        var text = messageTextService.buildCallText(game);
        render.withText(text);
    }
}
