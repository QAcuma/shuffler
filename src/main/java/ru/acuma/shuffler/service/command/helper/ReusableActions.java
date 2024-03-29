package ru.acuma.shuffler.service.command.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.RenderContext;
import ru.acuma.shuffler.context.StorageContext;
import ru.acuma.shuffler.context.cotainer.Render;
import ru.acuma.shuffler.context.cotainer.StorageTask;
import ru.acuma.shuffler.model.constant.StorageTaskType;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.event.EventStatusService;
import ru.acuma.shuffler.service.event.GameService;
import ru.acuma.shuffler.service.event.GameStatusService;

import static ru.acuma.shuffler.model.constant.EventStatus.WAITING;

@Service
@RequiredArgsConstructor
public class ReusableActions {

    private final GameService gameService;
    private final EventStatusService eventStatusService;
    private final GameStatusService gameStatusService;
    private final RenderContext renderContext;
    private final StorageContext storageContext;

    public void nextGame(final TEvent event) {
        storageContext.forEvent(event)
            .store(StorageTask.of(StorageTaskType.GAME_FINISHED, event.getCurrentGame()));
        switch (eventStatusService.resume(event)) {
            case PLAYING -> {
                gameService.beginGame(event);
                storageContext.forEvent(event)
                    .store(StorageTask.of(StorageTaskType.GAME_BEGINS, event.getCurrentGame()));
                renderContext.forEvent(event)
                    .render(Render.forUpdate(MessageType.LOBBY))
                    .render(Render.forUpdate(MessageType.GAME));
            }
            case WAITING -> renderContext.forEvent(event)
                .render(Render.forUpdate(MessageType.LOBBY))
                .render(Render.forDelete(MessageType.GAME));
        }
    }

    public void returnGame(final TEvent event) {
        var chatRender = renderContext.forEvent(event);
        if (eventStatusService.resume(event) != WAITING) {
            gameStatusService.active(event.getCurrentGame());
            chatRender.render(Render.forUpdate(MessageType.GAME));
        }
        chatRender.render(Render.forUpdate(MessageType.LOBBY));
    }
}
