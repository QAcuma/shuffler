package ru.acuma.shuffler.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.service.api.MessageService;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {

    private final EventContext eventContext;
    private final ExecuteService executeService;
    private final MessageService messageService;

    @Override
    public void sweepChat(TgEvent event) {
        event.getMessages().stream()
                .map(messageId -> messageService.deleteMessage(event.getChatId(), messageId))
                .peek(executeService::execute)
                .map(DeleteMessage::getMessageId)
                .forEach(event::missMessage);
    }

    @Override
    public void sweepMessage(Message message) {
        sweepMessage(message.getChatId(), message.getMessageId());
    }

    @SneakyThrows
    public void sweepMessage(Long chatId, Integer messageId) {
        CompletableFuture.supplyAsync(() -> messageService.deleteMessage(chatId, messageId))
                .thenAccept(executeService::execute)
                .thenApply(future -> eventContext.findEvent(chatId))
                .thenAccept(event -> event.missMessage(messageId));
    }

    @Override
    public void sweepEvent(TgEvent event) {
        eventContext.sweepEvent(event.getChatId());
    }
}
