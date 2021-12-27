package ru.acuma.k.shuffler.service.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.cache.EventContextService;

public abstract class BaseBotCommand extends BotCommand {

    @Autowired
    private EventContextService eventContextService;

    /**
     * Construct a command
     *
     * @param commandIdentifier the unique identifier of this command (e.g. the command string to
     *                          enter into chat)
     * @param description       the description of this command
     */
    public BaseBotCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        if (message.getChat() != null && eventContextService.isActive(message.getChatId())) {
            eventContextService.getEvent(message.getChatId()).getMessages().add(message.getMessageId());
        }
        super.processMessage(absSender, message, new String[]{message.getMessageId().toString()});
    }
}
