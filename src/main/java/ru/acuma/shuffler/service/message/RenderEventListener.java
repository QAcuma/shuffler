package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.model.domain.RenderEvent;

@Component
@RequiredArgsConstructor
public class RenderEventListener {

    private final RenderService renderService;

    @EventListener
    public void onRenderDeleteEvent(final RenderEvent renderEvent) {
        renderService.delete(renderEvent.getChatId(), renderEvent.getRender());
    }
}
