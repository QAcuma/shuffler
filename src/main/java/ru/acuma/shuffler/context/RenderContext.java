package ru.acuma.shuffler.context;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.domain.TEvent;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RenderContext {
    private final Map<Long, ChatRender> renderStorage = new ConcurrentHashMap<>();

    public ChatRender forChat(final Long chatId) {
        var chatRender = Optional.ofNullable(renderStorage.get(chatId))
            .orElse(ChatRender.of());
        renderStorage.putIfAbsent(chatId, chatRender);

        return chatRender;
    }

    public ChatRender forEvent(final TEvent event) {
        return forChat(event.getChatId());
    }

}
