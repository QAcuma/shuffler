package ru.acuma.shuffler.model.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.acuma.shuffler.model.constant.messages.ExecuteStrategy;
import ru.acuma.shuffler.model.constant.messages.MessageAction;
import ru.acuma.shuffler.model.constant.messages.MessageAfterAction;
import ru.acuma.shuffler.model.constant.messages.MessageType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Accessors(chain = true)
public class Render implements Serializable {

    private Integer messageId;
    private MessageType messageType;
    private ExecuteStrategy executeStrategy;
    private Long delay;
    private MessageAction messageAction;
    private Set<MessageAfterAction> afterActions;

    public boolean requireChanges() {
        return ExecuteStrategy.IDLE != executeStrategy;
    }

    public static Render forSend(final MessageType messageType) {
        return Render.builder()
            .messageType(messageType)
            .executeStrategy(ExecuteStrategy.REGULAR)
            .messageAction(MessageAction.SEND)
            .afterActions(new HashSet<>())
            .build();
    }

    public static Render forUpdate(final MessageType messageType, final Integer messageId) {
        return Render.builder()
            .messageId(messageId)
            .messageType(messageType)
            .executeStrategy(ExecuteStrategy.REGULAR)
            .messageAction(MessageAction.UPDATE)
            .afterActions(new HashSet<>())
            .build();
    }

    public static Render forMarkup(final MessageType messageType, final Integer messageId) {
        return Render.builder()
            .messageId(messageId)
            .messageType(messageType)
            .executeStrategy(ExecuteStrategy.REGULAR)
            .messageAction(MessageAction.UPDATE_MARKUP)
            .afterActions(new HashSet<>())
            .build();
    }

    public static Render forDelete(final Integer messageId) {
        return Render.builder()
            .messageId(messageId)
            .executeStrategy(ExecuteStrategy.REGULAR)
            .messageAction(MessageAction.DELETE)
            .afterActions(new HashSet<>())
            .build();
    }

    public Render withAfterAction(final MessageAfterAction afterAction) {
        afterActions.add(afterAction);

        return this;
    }

    public Render withDelay(final Long delay) {
        return setDelay(delay)
            .setExecuteStrategy(ExecuteStrategy.DELAYED);
    }

    public Render withSchedule() {
        return setExecuteStrategy(ExecuteStrategy.SCHEDULED);
    }

    public void success() {
        setExecuteStrategy(ExecuteStrategy.IDLE);
    }
}
