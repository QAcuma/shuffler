package ru.acuma.shuffler.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.CancelEvictCommand;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.service.api.EventFacade;
import ru.acuma.shuffler.service.aspect.CheckPlayerInEvent;
import ru.acuma.shuffler.service.aspect.SweepMessage;
import ru.acuma.shuffler.service.executor.CommandExecutorSourceFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.EVICTING;

@Service
@RequiredArgsConstructor
public class CancelEvictCommandHandler extends CommandHandler<CancelEvictCommand> {

    private final CommandExecutorSourceFactory commandExecutorFactory;
    private final EventFacade eventFacade;

    @Override
    protected void init() {
        commandExecutorFactory.register(EVICTING, getCommandClass(), getEvictingConsumer());
    }

    @Override
    public Class<CancelEvictCommand> getCommandClass() {
        return CancelEvictCommand.class;
    }

    @Override
    @SweepMessage
    @CheckPlayerInEvent
    public void handle(Message message) {
        super.handle(message);
    }

    private BiConsumer<Message, TgEvent> getEvictingConsumer() {
        return eventFacade.getCheckingConsumer();
    }
}
