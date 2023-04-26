package ru.acuma.shuffler.service.telegram;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.acuma.shuffler.controller.BaseBotCommand;
import ru.acuma.shuffler.service.user.AuthService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommandRouter {

    private final AuthService authService;
    private final List<BaseBotCommand> commands;
    private final TelegramCommandRegistry commandRegistry;

    @PostConstruct
    private void init() {
        commands.forEach(commandRegistry::register);
    }

    public void route(Update update) {
        if (update.getCallbackQuery() != null) {
            processCallback(update.getCallbackQuery());
        }
    }

    private void processCallback(CallbackQuery callbackQuery) {
        callbackQuery.getMessage().setFrom(callbackQuery.getFrom());
        if (filter(callbackQuery.getMessage())) {
            return;
        }
        var command = StringUtils.substringBefore(callbackQuery.getData(), "?");
        var args = StringUtils.substringAfter(callbackQuery.getData(), "?");
        commandRegistry.resolve(command).handleCommand(callbackQuery.getMessage(), new String[]{args});
    }

    private boolean filter(Message message) {
        return authService.provideAuth(message);
    }
}
