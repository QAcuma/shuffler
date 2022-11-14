package ru.acuma.shuffler.service.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.EventStateService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.GameStateService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.aspect.SweepMessage;
import ru.acuma.shuffler.service.command.CancelEvictCommand;
import ru.acuma.shuffler.service.executor.CommandExecutorFactory;

import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.EVICTING;

@Service
@RequiredArgsConstructor
public class CancelEvictCommandService extends CommandService<CancelEvictCommand> {

    private final CommandExecutorFactory commandExecutorFactory;
    private final EventContextServiceImpl eventContextService;
    private final EventStateService eventStateService;
    private final ExecuteService executeService;
    private final MessageService messageService;
    private final GameStateService gameStateService;

    @Override
    protected void init() {
        commandExecutorFactory.register(EVICTING, getCommandClass(), getEvictingConsumer());
    }

    @Override
    public Class<CancelEvictCommand> getCommandClass() {
        return CancelEvictCommand.class;
    }

    //TODO: добавить аннотацию на проверку прав игрока
    @Override
    @SweepMessage
    public void handle(Message message) {
        final var event = eventContextService.getCurrentEvent(message.getChatId());
        if (event == null || event.playerNotParticipate(message.getFrom().getId())) {
            return;
        }
        super.handle(message);
    }

    private BiConsumer<Message, TgEvent> getEvictingConsumer() {
        return (message, event) -> {
            eventStateService.defineActiveState(event);
            gameStateService.activeState(event.getLatestGame());
            executeService.execute(messageService.updateLobbyMessage(event));
            executeService.execute(messageService.updateMessage(event, event.getLatestGame().getMessageId(), MessageType.GAME));
        };
    }
}
