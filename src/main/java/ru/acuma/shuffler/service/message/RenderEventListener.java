package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.model.wrapper.RenderEvent;

@Component
@RequiredArgsConstructor
public class RenderEventListener {

    private final RenderService renderService;

    @EventListener(condition = "#renderEvent.render.messageAction == T(ru.acuma.shuffler.model.constant.messages.MessageAction).DELETE")
    public void onRenderDelete(final RenderEvent renderEvent) {
        renderService.delete(renderEvent.getChatId(), renderEvent.getRender());
    }
}
