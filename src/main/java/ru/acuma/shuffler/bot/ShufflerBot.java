package ru.acuma.shuffler.bot;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.acuma.shuffler.controller.BaseBotCommand;
import ru.acuma.shuffler.service.user.AuthService;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShufflerBot extends TelegramLongPollingCommandBot {

    private final List<BaseBotCommand> commands;
    private final AuthService authService;
    @Value("${telegram.bot.name}")
    private String botName;
    @Value("${telegram.bot.token}")
    private String botToken;

    @PostConstruct
    private void init() {
        commands.forEach(this::register);
    }

    @Override
    public boolean filter(Message message) {
        return authService.doAuth(message);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.getCallbackQuery() != null) {
            processCallback(update.getCallbackQuery());
        }
    }

    public final <T extends Serializable, M extends BotApiMethod<T>> T executeApiMethod(M method) throws TelegramApiException {
        return super.execute(method);
    }

    private void processCallback(CallbackQuery callbackQuery) {
        callbackQuery.getMessage().setFrom(callbackQuery.getFrom());
        if (filter(callbackQuery.getMessage())) {
            return;
        }
        String command = StringUtils.substringBefore(callbackQuery.getData(), "?");
        callbackQuery.getMessage().setText(StringUtils.substringAfter(callbackQuery.getData(), "?"));
        getRegisteredCommand(command).processMessage(
                this,
                callbackQuery.getMessage(),
                new String[]{}
        );
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
