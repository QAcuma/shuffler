package ru.acuma.shuffler.service.message.content;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.context.cotainer.Render;
import ru.acuma.shuffler.service.message.MessageTextService;

@Service
@RequiredArgsConstructor
public class CancelledContent implements Fillable, WithText<Void> {

    private final EventContext eventContext;
    private final MessageTextService messageTextService;

    @Override
    public void fill(final Render render, final Long chatId) {
        fillText(render, null);
    }

    @Override
    public void fillText(final Render render, final Void subject) {
        var text = messageTextService.buildCancelledContent();
        render.withText(text);
    }
}
