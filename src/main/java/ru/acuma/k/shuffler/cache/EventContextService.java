package ru.acuma.k.shuffler.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.model.domain.KickerEvent;
import ru.acuma.k.shuffler.model.enums.EventState;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventContextService {

    private final EventContext eventContext;

    public KickerEvent getEvent(Long chatId) {
        return eventContext.getEvents().get(chatId);
    }

    public KickerEvent buildEvent(Long chatId) {
        if (isActive(chatId)) {
            return getEvent(chatId);
        }
        KickerEvent event = KickerEvent.builder()
                .eventState(EventState.CREATED)
                .chatId(chatId)
                .startedAt(LocalDateTime.now())
                .build();
        addEvent(event);
        return event;
    }

    public boolean isActive(Long chatId) {
        var event = getEvent(chatId);
        if (event != null) {
            return !event.getEventState().equals(EventState.FINISHED);
        }
        return false;
    }

    public boolean isUserRegistered(Long chatId, User user) {
        return getEvent(chatId).getPlayers().contains(user);
    }

    public void registerMessage(Long chatId, Integer messageId) {
        getEvent(chatId).getMessages().add(messageId);
    }

    public void unregisterMessage(Long chatId, Integer messageId) {
        if (isActive(chatId)) {
            getEvent(chatId).getMessages().remove(messageId);
        }
    }

    public void registerPlayer(Long chatId, User user) {
        if (isActive(chatId)) {
            getEvent(chatId).getPlayers().add(user);
        }
    }

    public void unregisterPlayer(Long chatId, User user) {
        if (isActive(chatId)) {
            getEvent(chatId).getMessages().remove(user);
        }
    }

    public void finishEvent(KickerEvent event, boolean store) {
        event.setEventState(EventState.FINISHED);
        event.setFinishedAt(LocalDateTime.now());
        eventContext.getEvents().remove(event.getChatId());
    }

    private void addEvent(KickerEvent event) {
        eventContext.getEvents().put(event.getChatId(), event);
    }
}
