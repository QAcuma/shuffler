package ru.acuma.shuffler.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.aspect.marker.SweepMessage;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.exception.GlobalExceptionHandler;
import ru.acuma.shuffler.service.event.DataService;
import ru.acuma.shuffler.service.message.RenderService;
import ru.acuma.shuffler.service.telegram.filter.AuthFilter;
import ru.acuma.shuffler.service.telegram.filter.UpdateValidator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackService {

    private static final Map<Long, Object> chatIdLocks = new ConcurrentHashMap<>();

    private final List<UpdateValidator> updateValidators;
    private final List<AuthFilter> authFilters;
    private final TelegramCommandRegistry commandRegistry;
    private final EventContext eventContext;
    private final DataService dataService;
    private final RenderService renderService;
    private final GlobalExceptionHandler exceptionHandler;

    public void filter(final CallbackQuery callbackQuery) {
        callbackQuery.getMessage().setFrom(callbackQuery.getFrom());
        updateValidators.forEach(filter -> filter.accept(callbackQuery));
        authFilters.forEach(filter -> filter.accept(callbackQuery));
        var message = callbackQuery.getMessage();
        var commandText = callbackQuery.getData();

        accept(message, commandText);
    }

    @SweepMessage
    public void filter(final Message message) {
        updateValidators.forEach(filter -> filter.accept(message));
        authFilters.forEach(filter -> filter.accept(message));
        var commandText = message.getText();

        accept(message, commandText);
    }

    private void accept(final Message message, final String commandText) {
        var command = StringUtils.substringBefore(commandText, "?");
        var argSection = StringUtils.substringAfter(commandText, "?");
        var args = argSection.split("&");

        handle(command, message, args);
    }

    private void handle(final String command, final Message message, final String... args) {
        var chatId = message.getChatId();

        synchronized (chatIdLocks.computeIfAbsent(chatId, k -> new Object())) {
            eventContext.snapshotEvent(chatId);
            try {
                commandRegistry.resolve(command).execute(message, args);
                dataService.saveData(chatId);
                eventContext.snapshotEvent(chatId);
            } catch (final Exception exception) {
                exceptionHandler.handle(exception);
                eventContext.rollbackEvent(chatId);
            } finally {
                renderService.delete(chatId);
                chatIdLocks.remove(chatId);
            }
        }
    }
}
