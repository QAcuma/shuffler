package ru.acuma.shuffler.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
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
    public void sweepChat(TgEvent event) {
        Set<Integer> copyIds = new HashSet<>(event.getMessages());
        copyIds.forEach(id -> {
            var deleteMessage = messageService.deleteMessage(event.getChatId(), id);
            executeService.execute(deleteMessage);
            event.missMessage(id);
        });
    }

    @Override
    public void sweepMessage(Message message) {
        sweepMessage(message.getChatId(), message.getMessageId());
    }

    @SneakyThrows
    public void sweepMessage(Long chatId, Integer messageId) {
        final var event = eventContextService.getCurrentEvent(chatId);
        var deleteMessage = messageService.deleteMessage(chatId, messageId);
        executeService.execute(deleteMessage);
        if (eventContextService.isActive(chatId)) {
            event.missMessage(messageId);
        }
    }

    @Override
    public void sweepEvent(TgEvent event) {
        eventContextService.evictEvent(event.getChatId());
    }
}
