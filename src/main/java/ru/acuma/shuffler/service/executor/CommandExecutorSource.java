package ru.acuma.shuffler.service.executor;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.BaseBotCommand;
import ru.acuma.shuffler.model.entity.TgEvent;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface CommandExecutorSource<T> {

    BiConsumer<Message, TgEvent> getExecutor(T type, Class<? extends BaseBotCommand> command);

}
