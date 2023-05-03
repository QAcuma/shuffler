package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.model.domain.TEvent;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final EventContext eventContext;

    public void sweepChat(TEvent event) {
//        event.getMessages().values()
//            .stream()
//            .map(Render::getMessageId)
//            .map(messageId -> messageService.deleteMessage(event.getChatId(), messageId))
//            .peek(executeService::execute)
//            .forEach(DeleteMessage::getMessageId);
    }

//    public void sweepMessage(final Message message) {
//        Optional.ofNullable(eventContext.findEvent(message.getChatId()))
//            .ifPresent(event -> event.delete(message.getMessageId()));
//    }
//
//    public void sweepMessage(final Long chatId, final Integer messageId) {
//        Optional.ofNullable(eventContext.findEvent(chatId))
//            .ifPresent(event -> event.delete(messageId));
//    }

    public void flushEvent(TEvent event) {
        eventContext.flushEvent(event.getChatId());
    }
}
