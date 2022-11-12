package ru.acuma.shuffler.service.executor;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.service.command.BaseBotCommand;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface CommandRegister<T> {

    void register(T type, Class<? extends BaseBotCommand> command, BiConsumer<Message, TgEvent> consumer);

}
