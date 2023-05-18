package ru.acuma.shuffler.context;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.constant.messages.MessageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

@Getter
@EqualsAndHashCode
public class ChatRender {

    private final Map<MessageType, Render> renders = new EnumMap<>(MessageType.class);
    @EqualsAndHashCode.Exclude
    private final List<Future<?>> futures = new ArrayList<>();

    private ChatRender() {
    }

    public ChatRender render(final Render render) {
        Optional.ofNullable(renders.get(render.getMessageType()))
            .ifPresent(oldRender -> render.setMessageId(oldRender.getMessageId()));
        renders.put(render.getMessageType(), render);

        return this;
    }

    public Integer getMessageId(final MessageType messageType) {
        return Optional.ofNullable(renders.get(messageType))
            .map(Render::getMessageId)
            .orElseThrow(() -> new DataException(ExceptionCause.MESSAGE_NOT_FOUND, messageType));
    }

    public void schedule(final ScheduledFuture<Serializable> future) {
        futures.add(future);
    }

    public void flushFutures() {
        futures.forEach(future -> future.cancel(true));
        futures.clear();
    }

    public static ChatRender of() {
        return new ChatRender();
    }
}
