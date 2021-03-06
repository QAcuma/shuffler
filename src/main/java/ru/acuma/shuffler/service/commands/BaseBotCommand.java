package ru.acuma.shuffler.service.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.cache.EventContextServiceImpl;

@Slf4j
public abstract class BaseBotCommand extends BotCommand {

    @Autowired
    private EventContextServiceImpl eventContextService;

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
            var event = eventContextService.getCurrentEvent(message.getChatId());
            event.watchMessage(message.getMessageId());
        }
        execute(absSender, message);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
    }

    public abstract void execute(AbsSender absSender, Message message);

}
