package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.acuma.k.shuffler.cache.EventContextService;
import ru.acuma.k.shuffler.model.domain.KickerEvent;
import ru.acuma.k.shuffler.model.enums.Values;
import ru.acuma.k.shuffler.service.ExecuteService;
import ru.acuma.k.shuffler.service.KeyboardService;
import ru.acuma.k.shuffler.service.MessageService;

import java.io.Serializable;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ru.acuma.k.shuffler.model.enums.Values.CANCELLED_MESSAGE_TIMEOUT;
import static ru.acuma.k.shuffler.model.enums.messages.MessageType.CHECKING_TIMED;

@Service
@RequiredArgsConstructor
public class ExecuteServiceImpl implements ExecuteService {

    private final EventContextService eventContextService;
    private final MessageService messageService;
    private final KeyboardService keyboardService;

    @SneakyThrows
    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(AbsSender absSender, Method method) {
        var msg = absSender.execute(method);
        if (msg instanceof Message) {
            eventContextService.registerMessage(((Message) msg).getChatId(), ((Message) msg).getMessageId());
        }
        return msg;
    }

    @Override
    public void executeAsync(AbsSender absSender, Runnable method, Long delay) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(method, CANCELLED_MESSAGE_TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    public void executeAsyncTimer(AbsSender absSender, KickerEvent event, BotApiMethod<Message> message) {
        var msg = execute(absSender, message);
        eventContextService.registerMessage(msg.getChatId(), msg.getMessageId());
        var update = messageService.updateMessage(event, msg.getMessageId(), CHECKING_TIMED);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        for (int i = 1; i <= Values.TIMEOUT; i++) {
            final int current = i;
            executorService.schedule(() -> {
                update.setReplyMarkup(keyboardService.getTimedCheckingKeyboard(Values.TIMEOUT - current));
                return absSender.execute(update);
            }, current, TimeUnit.SECONDS);
        }
    }
}
