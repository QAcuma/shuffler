package ru.acuma.shuffler.context;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.cotainer.EventStorage;
import ru.acuma.shuffler.model.domain.TEvent;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class StorageContext {
    private final Map<Long, EventStorage> entityStorage = new ConcurrentHashMap<>();

    public EventStorage forChat(final Long chatId) {
        var chatRender = Optional.ofNullable(entityStorage.get(chatId))
            .orElse(EventStorage.of());
        entityStorage.putIfAbsent(chatId, chatRender);

        return chatRender;
    }

    public EventStorage forEvent(final TEvent event) {
        return forChat(event.getChatId());
    }
}
