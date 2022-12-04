package ru.acuma.shuffler.service.executor;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.controller.BaseBotCommand;

@FunctionalInterface
public interface Executor {

    <T extends BaseBotCommand> void doExecute(Message message, Class<T> commandClass);

}
