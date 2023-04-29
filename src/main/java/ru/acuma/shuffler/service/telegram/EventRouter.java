package ru.acuma.shuffler.service.telegram;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.acuma.shuffler.controller.BaseBotCommand;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EventRouter {

    private final List<BaseBotCommand> commands;
    private final TelegramCommandRegistry commandRegistry;
    private final CallbackService callbackService;

    @PostConstruct
    private void init() {
        commands.forEach(commandRegistry::register);
    }

    public void route(final Update update) {
        if (update.getCallbackQuery() != null) {
            callbackService.filter(update.getCallbackQuery());
        } else if (Objects.nonNull(update.getMessage()) && isCommandMessage(update.getMessage())) {
            callbackService.filter(update.getMessage());
        }
    }

    private boolean isCommandMessage(final Message message) {
        var text = message.getText();

        return StringUtils.isNotBlank(text) && StringUtils.startsWith(text, "/");
    }
}
