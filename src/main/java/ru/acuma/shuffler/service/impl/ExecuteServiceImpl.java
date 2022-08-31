package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.acuma.shuffler.cache.EventContextServiceImpl;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.Values;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.KeyboardService;
import ru.acuma.shuffler.service.api.MessageService;

import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecuteServiceImpl implements ExecuteService {

    public static final int TOO_MANY_REQUESTS_CODE = 429;
    public static final String TIMEOUT_REGEX = "(?<=after )\\d+$";

    private final EventContextServiceImpl eventContextService;
    private final MessageService messageService;
    private final KeyboardService keyboardService;

    @SneakyThrows
    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(AbsSender absSender, Method method) {
        try {
            var msg = absSender.execute(method);
            if (msg instanceof Message) {
                var message = (Message) msg;
                final var event = eventContextService.getCurrentEvent(message.getChatId());
                if (event != null && message.getMessageId() != null) {
                    event.watchMessage(message.getMessageId());
                }
            }
            return msg;
        } catch (TelegramApiRequestException e) {
            log.warn(e.getMessage());
            if (e.getErrorCode() == TOO_MANY_REQUESTS_CODE) {
                Pattern pattern = Pattern.compile(TIMEOUT_REGEX);
                Matcher matcher = pattern.matcher(e.getMessage());
                if (matcher.find()) {
                    Thread.sleep(Long.parseLong(matcher.group(0)) * 1000);
                } else {
                    Thread.sleep(5000L);
                }
                return execute(absSender, method);
            }
        }
        return null;
    }

    @Override
    public void executeLater(AbsSender absSender, Runnable method, int delay) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(method, delay, TimeUnit.SECONDS);
    }

    @Override
    public void executeAsyncTimer(AbsSender absSender, TgEvent event, BotApiMethod<Message> message) {
        var msg = execute(absSender, message);
        event.watchMessage(msg.getMessageId());
        var update = messageService.updateMarkup(event, msg.getMessageId(), MessageType.CHECKING_TIMED);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        for (int i = 1; i <= Values.TIMEOUT; i++) {
            final int current = i;
            executorService.schedule(() -> {
                update.setReplyMarkup(keyboardService.getTimedKeyboard(Values.TIMEOUT - current));
                try {
                    return absSender.execute(update);
                } catch (TelegramApiRequestException e) {
                    log.warn(e.getMessage());
                    if (e.getErrorCode() == TOO_MANY_REQUESTS_CODE) {
                        Pattern pattern = Pattern.compile(TIMEOUT_REGEX);
                        Matcher matcher = pattern.matcher(e.getMessage());
                        if (matcher.find()) {
                            Thread.sleep(Long.parseLong(matcher.group(0)) * 1000);
                        } else {
                            Thread.sleep(5000L);
                        }
                    }
                    return absSender.execute(update);
                }
            }, current, TimeUnit.SECONDS);
        }
    }
}
