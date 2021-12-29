package ru.acuma.k.shuffler.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.acuma.k.shuffler.service.GroupService;
import ru.acuma.k.shuffler.service.NonCommandService;
import ru.acuma.k.shuffler.service.UserService;
import ru.acuma.k.shuffler.service.commands.BeginCommand;
import ru.acuma.k.shuffler.service.commands.CancelCommand;
import ru.acuma.k.shuffler.service.commands.JoinCommand;
import ru.acuma.k.shuffler.service.commands.KickerCommand;
import ru.acuma.k.shuffler.service.commands.LeaveCommand;
import ru.acuma.k.shuffler.service.commands.NoCommand;
import ru.acuma.k.shuffler.service.commands.ResetCommand;
import ru.acuma.k.shuffler.service.commands.ShuffleCommand;
import ru.acuma.k.shuffler.service.commands.WaitCommand;
import ru.acuma.k.shuffler.service.commands.YesCommand;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Slf4j
public class KickerBot extends TelegramLongPollingCommandBot {

    private final String BOT_NAME;
    private final String BOT_TOKEN;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private NonCommandService nonCommandService;

    @Autowired
    private KickerCommand kickerCommand;

    @Autowired
    private JoinCommand joinCommand;

    @Autowired
    private LeaveCommand leaveCommand;

    @Autowired
    private CancelCommand cancelCommand;

    @Autowired
    private ShuffleCommand shuffleCommand;

    @Autowired
    private YesCommand yesCommand;

    @Autowired
    private NoCommand noCommand;

    @Autowired
    private WaitCommand waitCommand;

    @Autowired
    private BeginCommand beginCommand;

    @Autowired
    private ResetCommand resetCommand;


    public KickerBot(String botName, String botToken) {
        super();
        BOT_NAME = botName;
        BOT_TOKEN = botToken;
    }

    @PostConstruct
    private void init() {
        register(kickerCommand);
        register(joinCommand);
        register(leaveCommand);
        register(cancelCommand);
        register(shuffleCommand);
        register(yesCommand);
        register(noCommand);
        register(waitCommand);
        register(beginCommand);
        register(resetCommand);
    }

    @Override
    protected boolean filter(Message message) {
        return groupService.authenticate(message.getChat()) && !userService.authenticate(message.getFrom());
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.getCallbackQuery() != null) {
            processCallback(update.getCallbackQuery());
        }
        reply(nonCommandService.process(update));
    }

    private void processCallback(CallbackQuery callbackQuery) {
        callbackQuery.getMessage().setFrom(callbackQuery.getFrom());
        getRegisteredCommand(callbackQuery.getData()).processMessage(this,
                callbackQuery.getMessage(),
                new String[]{}
        );
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
