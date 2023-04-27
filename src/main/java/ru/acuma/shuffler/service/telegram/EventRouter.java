package ru.acuma.shuffler.service.telegram;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.acuma.shuffler.controller.BaseBotCommand;

import java.util.List;

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
            callbackService.accept(update.getCallbackQuery());
        }
    }
}
