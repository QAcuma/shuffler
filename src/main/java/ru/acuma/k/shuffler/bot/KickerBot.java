package ru.acuma.k.shuffler.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.acuma.k.shuffler.service.NonCommandService;
import ru.acuma.k.shuffler.service.commands.*;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Slf4j
public class KickerBot extends TelegramLongPollingCommandBot {

    private final String BOT_NAME;
    private final String BOT_TOKEN;

    @Autowired
    private NonCommandService nonCommandService;

    @Autowired
    private KickerCommand kickerCommand;

    @Autowired
    private JoinCommand joinCommand;

    @Autowired
    private LeaveCommand leaveCommand;

    @Autowired
    private ShuffleCommand shuffleCommand;

    @Autowired
    private BeginCommand beginCommand;

    public KickerBot(String botName,
                     String botToken) {
        super();
        BOT_NAME = botName;
        BOT_TOKEN = botToken;
    }

    @PostConstruct
    private void init(){
        register(kickerCommand);
        register(joinCommand);
        register(leaveCommand);
        register(shuffleCommand);
        register(beginCommand);
    }

    @Override
    protected boolean filter(Message message) {
        return !message.getChat().isGroupChat();
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        reply(nonCommandService.process(update));
    }

    public <M extends SendMessage> void reply(M message) {
        if (Objects.isNull(message)) {
            return;
        }
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send answer", e);
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

}
