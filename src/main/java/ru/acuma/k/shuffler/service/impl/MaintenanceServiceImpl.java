package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.acuma.k.shuffler.cache.EventContextService;
import ru.acuma.k.shuffler.model.domain.KickerEvent;
import ru.acuma.k.shuffler.service.ExecuteService;
import ru.acuma.k.shuffler.service.MaintenanceService;
import ru.acuma.k.shuffler.service.MessageService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {

    private final EventContextService eventContextService;
    private final ExecuteService executeService;
    private final MessageService messageService;

    @Override
    public void sweepChat(AbsSender absSender, Set<Integer> messages, Long chatId) {
        Set<Integer> copyIds = new HashSet<>(messages);
        copyIds.forEach(id -> {
            var deleteMessage = messageService.deleteMessage(chatId, id);
            try {
                executeService.execute(absSender, deleteMessage);
                eventContextService.unregisterMessage(chatId, id);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        });
    }

    @Override
    public void sweepContext(AbsSender absSender, String[] args, Long chatId) {
        Integer messageId = Integer.valueOf(Arrays.stream(args).findFirst().orElse("0"));
        sweepChat(absSender, Collections.singleton(messageId), chatId);
        eventContextService.unregisterMessage(chatId, messageId);
    }

    @Override
    public void sweepEvent(KickerEvent event, boolean store) {
        eventContextService.finishEvent(event, store);
    }
}
