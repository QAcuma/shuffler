package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.model.domain.KickerEvent;
import ru.acuma.k.shuffler.service.DelayedService;
import ru.acuma.k.shuffler.service.KeyboardService;
import ru.acuma.k.shuffler.util.BuildMessageUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DelayedServiceImpl implements DelayedService {

    private static final int TIMEOUT = 3;

    private final KeyboardService keyboardService;

    @Override
    public int getTimeout() {
        return TIMEOUT;
    }

    @SneakyThrows
    @Override
    public void processCheckingTimer(AbsSender absSender, SendMessage message, KickerEvent event) {
        var messageId = absSender.execute(message).getMessageId();

        EditMessageText update = EditMessageText.builder()
                .chatId(String.valueOf(event.getGroupId()))
                .messageId(messageId)
                .text(BuildMessageUtil.buildCancelMessage(event))
                .build();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        for (int i = 1; i <= TIMEOUT; i++) {
            final int current = i;
            executorService.schedule(() -> {
                update.setReplyMarkup(keyboardService.getTimedCheckingKeyboard(TIMEOUT - current));
                return absSender.execute(update);
            }, current, TimeUnit.SECONDS);
        }
    }
}
