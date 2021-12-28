package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.acuma.k.shuffler.cache.EventContextService;
import ru.acuma.k.shuffler.model.domain.KickerEvent;
import ru.acuma.k.shuffler.service.MaintenanceService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {

    private final EventContextService eventContextService;

    @Override
    public void sweepChat(AbsSender absSender, Set<Integer> messages, Long groupId) {
        messages.forEach(id -> {
            DeleteMessage deleteMessage = DeleteMessage.builder()
                    .chatId(String.valueOf(groupId))
                    .messageId(id)
                    .build();
            try {
                absSender.execute(deleteMessage);
                eventContextService.unregisterMessage(groupId, id);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        });
    }

    @Override
    public void sweepFromArgs(AbsSender absSender, String[] args, Long groupId) {
        Integer messageId = Integer.valueOf(Arrays.stream(args).findFirst().orElse("0"));
        sweepChat(absSender, Collections.singleton(messageId), groupId);
        eventContextService.unregisterMessage(groupId, messageId);
    }

    @Override
    public void sweepEvent(KickerEvent event, boolean store) {
        eventContextService.finishEvent(event, store);
    }
}
