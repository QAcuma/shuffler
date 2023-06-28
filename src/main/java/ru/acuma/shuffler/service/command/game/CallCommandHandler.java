package ru.acuma.shuffler.service.command.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.context.cotainer.Render;
import ru.acuma.shuffler.controller.CallCommand;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.command.common.BaseCommandHandler;
import ru.acuma.shuffler.service.event.GameService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.acuma.shuffler.model.constant.EventStatus.PLAYING;
import static ru.acuma.shuffler.model.constant.EventStatus.WAITING_WITH_GAME;

@Service
@RequiredArgsConstructor
public class CallCommandHandler extends BaseCommandHandler<CallCommand> {

    private final GameService gameService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(PLAYING, WAITING_WITH_GAME);
    }

    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
        Optional.ofNullable(event.getCurrentGame())
            .filter(game -> Objects.isNull(game.getCalledBy()))
            .ifPresent(game -> {
                gameService.callPlayers(event.getCurrentGame(), user.getId());

                var chatRender = renderContext.forEvent(event);
                chatRender.render(Render.forMarkup(MessageType.GAME))
                    .render(Render.forSend(MessageType.CALL).withAfterAction(
                        () -> Render.forDelete(chatRender.getMessageId(MessageType.CALL))
                            .withDelay(Constants.CALL_MESSAGE_TTL_BEFORE_DELETE)
                    ));
            });
    }
}
