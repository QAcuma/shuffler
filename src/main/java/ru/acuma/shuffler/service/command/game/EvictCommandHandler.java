package ru.acuma.shuffler.service.command.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.controller.EvictCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.keyboards.CallbackParam;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.command.common.BaseCommandHandler;
import ru.acuma.shuffler.service.command.helper.ReusableActions;
import ru.acuma.shuffler.service.event.GameStatusService;
import ru.acuma.shuffler.service.event.PlayerService;
import ru.acuma.shuffler.util.ArgumentUtil;

import java.util.List;

import static ru.acuma.shuffler.model.constant.EventStatus.GAME_CHECKING;

@Service
@RequiredArgsConstructor
public class EvictCommandHandler extends BaseCommandHandler<EvictCommand> {

    private final GameStatusService gameStatusService;
    private final ReusableActions reusableActions;
    private final PlayerService playerService;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(GAME_CHECKING);
    }

    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
        var userId = ArgumentUtil.extractParam(CallbackParam.USER_ID, Long::valueOf, args);

        playerService.leaveEvent(userId, event);
        gameStatusService.cancelled(event.getCurrentGame());
        reusableActions.nextGame(event);
    }
}
