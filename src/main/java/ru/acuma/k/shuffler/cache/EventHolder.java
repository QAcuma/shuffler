package ru.acuma.k.shuffler.cache;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.model.domain.KickerEvent;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventHolder {

    private final Map<Long, KickerEvent> events = new HashMap<>();

    public boolean isActive(Long groupId) {
        return events.containsKey(groupId);
    }

    public boolean isRegistered(Long groupId, User user){
        return getEvent(groupId).getMembers().contains(user);
    }

    public void addEvent(KickerEvent kickerEvent) {
        events.put(kickerEvent.getGroupId(), kickerEvent);
    }

    public void endEvent(Long groupId){
        events.remove(groupId);
    }

    public KickerEvent getEvent(Long groupId) {
        return events.get(groupId);
    }

    public void setSourceMessage(Long groupId, Integer messageId) {
        var event = getEvent(groupId);
        if (event == null) {
            return;
        }
        event.setMessageId(messageId);
        events.put(groupId, event);
    }

    public void addEventMember(Long groupId, User user) {
        var event = getEvent(groupId);
        if (event == null) {
            return;
        }
        event.getMembers().add(user);
        events.put(groupId, event);
    }

    public void removeEventMember(Long groupId, User user) {
        var event = getEvent(groupId);
        if (event == null) {
            return;
        }
        event.getMembers().remove(user);
        events.put(groupId, event);
    }
}
