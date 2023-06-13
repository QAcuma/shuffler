package ru.acuma.shuffler.service.command.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.controller.CancelEvictCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.command.common.BaseCommandHandler;
import ru.acuma.shuffler.service.command.helper.ReusableActions;

import java.util.List;

import static ru.acuma.shuffler.model.constant.EventStatus.GAME_CHECKING;

@Service
@RequiredArgsConstructor
public class CancelEvictCommandHandler extends BaseCommandHandler<CancelEvictCommand> {

    private final ReusableActions reusableActions;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(GAME_CHECKING);
    }

    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
        reusableActions.returnGame(event);
    }
}
