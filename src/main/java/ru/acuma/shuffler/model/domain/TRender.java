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
public class TRender implements Serializable {

    private Integer messageId;
    private MessageType messageType;
    private ExecuteStrategy executeStrategy;
    private Integer delay;
    private MessageAction messageAction;
    private Set<MessageAfterAction> afterActions;

    public boolean requireChanges() {
        return ExecuteStrategy.IDLE != executeStrategy;
    }

    public static TRender forSend(final MessageType messageType) {
        return TRender.builder()
            .messageType(messageType)
            .executeStrategy(ExecuteStrategy.REGULAR)
            .messageAction(MessageAction.SEND)
            .afterActions(new HashSet<>())
            .build();
    }

    public static TRender forUpdate(final MessageType messageType, final Integer messageId) {
        return TRender.builder()
            .messageId(messageId)
            .messageType(messageType)
            .executeStrategy(ExecuteStrategy.REGULAR)
            .messageAction(MessageAction.UPDATE)
            .afterActions(new HashSet<>())
            .build();
    }

    public static TRender forMarkup(final MessageType messageType, final Integer messageId) {
        return TRender.builder()
            .messageId(messageId)
            .messageType(messageType)
            .executeStrategy(ExecuteStrategy.REGULAR)
            .messageAction(MessageAction.UPDATE_MARKUP)
            .afterActions(new HashSet<>())
            .build();
    }

    public static TRender forDelete(final Integer messageId) {
        return TRender.builder()
            .messageId(messageId)
            .executeStrategy(ExecuteStrategy.REGULAR)
            .messageAction(MessageAction.DELETE)
            .afterActions(new HashSet<>())
            .build();
    }

    public TRender withAfterAction(final MessageAfterAction afterAction) {
        afterActions.add(afterAction);

        return this;
    }

    public TRender withDelay(final Integer delay) {
        return setExecuteStrategy(ExecuteStrategy.DELAYED)
            .setDelay(delay);
    }

    public TRender withTimer() {
        return setExecuteStrategy(ExecuteStrategy.TIMER);
    }

    public void success() {
        setExecuteStrategy(ExecuteStrategy.IDLE);
    }
}
