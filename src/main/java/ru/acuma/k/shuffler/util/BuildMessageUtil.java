package ru.acuma.k.shuffler.util;

import ru.acuma.k.shuffler.model.domain.KickerEvent;
import ru.acuma.k.shuffler.model.enums.messages.EventConstant;
import ru.acuma.k.shuffler.service.enums.EventConstantApi;

import java.util.stream.Collectors;

public final class BuildMessageUtil {

    private BuildMessageUtil() {
    }

    public static String buildCreatedMessage(KickerEvent event) {
        EventConstantApi header = EventConstant.LOBBY_MESSAGE;
        return header.getText() + event.getMembers().stream()
                .map(user -> user.getFirstName() +
                        " " +
                        user.getLastName())
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public static String buildCheckingMessage(KickerEvent event) {
        switch (event.getEventState()) {
            case CANCEL_CHECKING:
                return EventConstant.CANCEL_CHECKING_MESSAGE.getText();
            case BEGIN_CHECKING:
                return EventConstant.BEGIN_CHECKING_MESSAGE.getText();
            case MEMBER_CHECKING:
                return EventConstant.MEMBER_CHECKING_MESSAGE.getText();
            case FINISH_CHECKING:
                return EventConstant.FINISH_CHECKING_MESSAGE.getText();
            default:
                return EventConstant.UNEXPECTED_CHECKING_MESSAGE.getText();
        }
    }
}
