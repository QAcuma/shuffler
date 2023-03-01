package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.RedCommand;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.GameStateService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.PLAYING;
import static ru.acuma.shuffler.model.enums.EventState.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class RedCommandHandler extends BaseCommandHandler<RedCommand> {

    private final GameStateService gameStateService;
    private final EventStateService eventStateService;
    private final EventFacade eventFacade;

    @Override
    protected List<EventState> getSupportedStates() {
        return List.of(PLAYING, WAITING_WITH_GAME);
    }

    @Override
    public void handle(Message message) {

    }

    private BiConsumer<Message, TgEvent> getPlayingWaitingWithGameConsumer() {
        return (message, event) -> {
            eventStateService.gameCheck(event);
            gameStateService.redCheck(event.getLatestGame());
            eventFacade.checkingStateActions(event);
        };
    }
}
