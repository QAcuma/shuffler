package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.service.api.EventContextService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;

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
    public void sweepChat(AbsSender absSender, TgEvent event) {
        Set<Integer> copyIds = new HashSet<>(event.getMessages());
        copyIds.forEach(id -> {
            var deleteMessage = messageService.deleteMessage(event.getChatId(), id);
            try {
                executeService.execute(absSender, deleteMessage);
                event.missMessage(id);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        });
    }

    @Override
    public void sweepMessage(AbsSender absSender, Message message) {
        sweepMessage(absSender, message.getChatId(), message.getMessageId());
    }

    @SneakyThrows
    public void sweepMessage(AbsSender absSender, Long chatId, Integer messageId) {
        final var event = eventContextService.getCurrentEvent(chatId);
        var deleteMessage = messageService.deleteMessage(chatId, messageId);
        executeService.execute(absSender, deleteMessage);
        if (eventContextService.isActive(chatId)) {
            event.missMessage(messageId);
        }
    }

    @Override
    public void sweepEvent(TgEvent event, boolean store) {
        eventContextService.evictEvent(event.getChatId());
    }
}
