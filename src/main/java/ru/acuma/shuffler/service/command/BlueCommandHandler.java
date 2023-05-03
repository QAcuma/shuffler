package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.controller.BlueCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TRender;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.event.EventStatusService;

import java.util.List;

import static ru.acuma.shuffler.model.constant.EventStatus.PLAYING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class BlueCommandHandler extends BaseCommandHandler<BlueCommand> {

    private final GameStateService gameStateService;
    private final EventStatusService eventStateService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(PLAYING, WAITING_WITH_GAME);
    }

    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
        eventStateService.gameCheck(event);
        gameStateService.blueCheck(event.getCurrentGame());

        event.render(TRender.forMarkup(MessageType.LOBBY, event.getMessageId(MessageType.LOBBY)));
        event.render(TRender.forMarkup(MessageType.GAME, event.getMessageId(MessageType.GAME)));
        event.render(TRender.forSend(MessageType.CHECKING_TIMED));
    }
}
