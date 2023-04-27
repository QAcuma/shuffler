package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.model.domain.TgEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final EventContext eventContext;

    public void sweepChat(TgEvent event) {
//        event.getMessages().values()
//            .stream()
//            .map(Render::getMessageId)
//            .map(messageId -> messageService.deleteMessage(event.getChatId(), messageId))
//            .peek(executeService::execute)
//            .forEach(DeleteMessage::getMessageId);
    }

    public void sweepMessage(Message message) {
        sweepMessage(message.getChatId(), message.getMessageId());
    }

    @SneakyThrows
    public void sweepMessage(Long chatId, Integer messageId) {
//        CompletableFuture.supplyAsync(() -> messageService.deleteMessage(chatId, messageId))
//            .thenAccept(executeService::execute)
//            .thenApply(future -> eventContext.findEvent(chatId));
    }

    public void flushEvent(TgEvent event) {
        eventContext.flushEvent(event.getChatId());
    }
}
