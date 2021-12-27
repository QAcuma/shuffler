package ru.acuma.k.shuffler.service.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventHolder;

public abstract class CallbackBotCommand extends BotCommand {

    @Autowired
    private EventHolder eventHolder;

    /**
     * Construct a command
     *
     * @param commandIdentifier the unique identifier of this command (e.g. the command string to
     *                          enter into chat)
     * @param description       the description of this command
     */
    public CallbackBotCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        eventHolder.setSourceMessage(message.getChatId(), message.getMessageId());
        super.processMessage(absSender, message, arguments);
    }
}
