package ru.acuma.shuffler.service.telegram;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.service.message.RenderService;
import ru.acuma.shuffler.service.telegram.filter.AuthFilter;
import ru.acuma.shuffler.service.telegram.filter.CallbackFilter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CallbackService {

    private static final Map<Long, Object> chatIdLocks = new ConcurrentHashMap<>();

    private final List<CallbackFilter> callbackFilters;
    private final List<AuthFilter> authFilters;
    private final TelegramCommandRegistry commandRegistry;
    private final EventContext eventContext;
    private final RenderService renderService;

    public void accept(final CallbackQuery callbackQuery) {
        callbackQuery.getMessage().setFrom(callbackQuery.getFrom());
        callbackFilters.forEach(filter -> filter.accept(callbackQuery));
        authFilters.forEach(filter -> filter.accept(callbackQuery));

        var command = StringUtils.substringBefore(callbackQuery.getData(), "?");
        var message = callbackQuery.getMessage();
        var argSection = StringUtils.substringAfter(callbackQuery.getData(), "?");
        var args = argSection.split("&");

        handle(command, message, args);
    }

    public final void handle(final String command, final Message message, final String... args) {
        var chatId = message.getChatId();

        synchronized (chatIdLocks.computeIfAbsent(chatId, k -> new Object())) {
            eventContext.snapshotEvent(chatId);
            try {
                commandRegistry.resolve(command).execute(message, args);
            } catch (Exception e) {
                eventContext.rollbackEvent(chatId);
            } finally {
                renderService.render(chatId);
                chatIdLocks.remove(chatId);
            }
        }
    }
}
