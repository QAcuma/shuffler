package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.controller.BlueCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.GameStateService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.constant.EventStatus.READY;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class BlueCommandHandler extends BaseCommandHandler<BlueCommand> {

    private final GameStateService gameStateService;
    private final EventStateService eventStateService;
    private final EventFacade eventFacade;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(READY, WAITING_WITH_GAME);
    }


    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {

    }

    private BiConsumer<Message, TEvent> getPlayingWaitingWithGameConsumer() {
        return (message, event) -> {
            eventStateService.gameCheck(event);
            gameStateService.blueCheck(event.getLatestGame());
            eventFacade.checkingStateActions(event);
        };
    }
}
