package ru.acuma.shuffler.service.message.type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.context.Render;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.message.KeyboardService;
import ru.acuma.shuffler.service.message.MessageContentService;

@Service
@RequiredArgsConstructor
public class CancelledContent implements Fillable, WithText<Void> {

    private final EventContext eventContext;
    private final MessageContentService messageContentService;

    @Override
    public void fill(final Render render, final Long chatId) {
        fillText(render, null);
    }

    @Override
    public void fillText(final Render render, final Void subject) {
        var text = messageContentService.buildCancelledContent();
        render.withText(text);
    }
}
