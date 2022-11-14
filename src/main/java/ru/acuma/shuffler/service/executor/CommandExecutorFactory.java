package ru.acuma.shuffler.service.executor;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shuffler.service.command.BaseBotCommand;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import static ru.acuma.shuffler.model.enums.EventState.ANY;

@Component
public class CommandExecutorFactory implements MessageExecutor<EventState>, CommandRegister<EventState> {

    private final Map<EventState, Map<Class<? extends BaseBotCommand>, BiConsumer<Message, TgEvent>>> executors;

    public CommandExecutorFactory() {
        this.executors = new HashMap<>();
    }

    @PostConstruct
    private void init() {
        Arrays.stream(EventState.values()).forEach(this::register);
    }

    private void register(EventState eventState) {
        executors.put(eventState, new HashMap<>());
    }

    @Override
    public BiConsumer<Message, TgEvent> getExecutor(EventState type, Class<? extends BaseBotCommand> command) {
        return Optional.ofNullable(executors.get(type).get(command))
                .orElse(executors.get(ANY).get(command));
    }

    @Override
    public void register(EventState type, Class<? extends BaseBotCommand> command, BiConsumer<Message, TgEvent> consumer) {
        executors.get(type).put(command, consumer);
    }

}
