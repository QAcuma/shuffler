package ru.acuma.shuffler.context.cotainer;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.messages.ExecuteStrategy;
import ru.acuma.shuffler.model.constant.messages.MessageAction;
import ru.acuma.shuffler.model.constant.messages.MessageType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Data
@Builder
@Accessors(chain = true)
public class Render implements Serializable {

    private Integer messageId;
    private MessageType messageType;
    private ExecuteStrategy executeStrategy;
    private Integer delay;
    private MessageAction messageAction;
    @Setter(AccessLevel.PRIVATE)
    private String messageText;
    @Setter(AccessLevel.PRIVATE)
    private InlineKeyboardMarkup keyboard;
    @EqualsAndHashCode.Exclude
    private transient List<Supplier<Render>> afterActions;
    private int hashCode;

    public boolean requireUpdate() {
        return ExecuteStrategy.IDLE != executeStrategy;
    }

    public static Render forSend(final MessageType messageType) {
        return Render.builder()
            .messageType(messageType)
            .executeStrategy(ExecuteStrategy.REGULAR)
            .messageAction(MessageAction.SEND)
            .afterActions(new ArrayList<>())
            .build();
    }

    public static Render forUpdate(final MessageType messageType) {
        return Render.builder()
            .messageType(messageType)
            .executeStrategy(ExecuteStrategy.REGULAR)
            .messageAction(MessageAction.UPDATE)
            .afterActions(new ArrayList<>())
            .build();
    }

    public static Render forMarkup(final MessageType messageType) {
        return Render.builder()
            .messageType(messageType)
            .executeStrategy(ExecuteStrategy.REGULAR)
            .messageAction(MessageAction.UPDATE_MARKUP)
            .afterActions(new ArrayList<>())
            .build();
    }

    public static Render forDelete(final Integer messageId) {
        return Render.builder()
            .messageId(messageId)
            .executeStrategy(ExecuteStrategy.REGULAR)
            .messageAction(MessageAction.DELETE)
            .afterActions(new ArrayList<>())
            .build();
    }

    public static Render forDelete(final MessageType messageType) {
        return Render.builder()
            .messageType(messageType)
            .executeStrategy(ExecuteStrategy.REGULAR)
            .messageAction(MessageAction.DELETE)
            .afterActions(new ArrayList<>())
            .build();
    }

    public static Render forPin(final Integer messageId) {
        return Render.builder()
            .messageId(messageId)
            .executeStrategy(ExecuteStrategy.REGULAR)
            .messageAction(MessageAction.PIN)
            .afterActions(new ArrayList<>())
            .build();
    }

    public Render withAfterAction(final Supplier<Render> afterAction) {
        afterActions.add(afterAction);

        return this;
    }

    public Render withDelay(final Integer delay) {
        return setExecuteStrategy(ExecuteStrategy.DELAYED)
            .setDelay(delay);
    }

    public Render withKeyboard(final InlineKeyboardMarkup keyboard) {
        return setKeyboard(keyboard);
    }

    public Render withText(final String text) {
        return setMessageText(text);
    }

    public Render withTimer() {
        return setExecuteStrategy(ExecuteStrategy.TIMER);
    }

    public void success() {
        setExecuteStrategy(ExecuteStrategy.IDLE);
    }

    public Integer getDelay() {
        return Objects.isNull(delay) ? Constants.DISABLED_BUTTON_TIMEOUT : delay;
    }
}
