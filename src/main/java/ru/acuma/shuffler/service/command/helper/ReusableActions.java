package ru.acuma.shuffler.service.command.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.Render;
import ru.acuma.shuffler.context.RenderContext;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.event.EventStatusService;
import ru.acuma.shuffler.service.event.GameService;

@Service
@RequiredArgsConstructor
public class ReusableActions {

    private final GameService gameService;
    private final EventStatusService eventStatusService;
    private final RenderContext renderContext;

    public void nextGame(final TEvent event) {
        gameService.finishGame(event);
        var chatRender = renderContext.forEvent(event);
        switch (eventStatusService.resume(event)) {
            case PLAYING -> {
                gameService.beginGame(event);
                chatRender.render(Render.forUpdate(MessageType.LOBBY))
                    .render(Render.forUpdate(MessageType.GAME));
            }
            case WAITING -> chatRender.render(Render.forUpdate(MessageType.LOBBY))
                .render(Render.forDelete(MessageType.GAME));
        }
    }
}
