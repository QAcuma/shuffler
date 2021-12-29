package ru.acuma.k.shuffler.util;

import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerPlayer;
import ru.acuma.k.shuffler.model.enums.messages.EventConstant;
import ru.acuma.k.shuffler.model.enums.messages.MessageType;
import ru.acuma.k.shuffler.service.enums.EventConstantApi;

import java.util.stream.Collectors;

import static ru.acuma.k.shuffler.model.enums.messages.EventConstant.DEFAULT_MESSAGE;

public final class BuildMessageUtil {

    private BuildMessageUtil() {
    }

    public static String buildText(KickerEvent event, MessageType type) {
        switch (type) {
            case LOBBY:
            case CANCELLED:
                return buildLobbyText(event);
            case CHECKING:
            case CHECKING_TIMED:
                return buildCheckingText(event);
            case GAME:
                return buildGameText(event);
            case STAT:
                return buildStatText(event);
            default:
                return DEFAULT_MESSAGE.getText();
        }
    }

    private static String buildGameText(KickerEvent event) {
        return EventConstant.PLAYING_MESSAGE.getText();
    }

    private static String buildLobbyText(KickerEvent event) {
        EventConstantApi header;
        switch (event.getEventState()) {
            case CREATED:
            case READY:
            case CANCEL_CHECKING:
            case BEGIN_CHECKING:
                header = EventConstant.LOBBY_MESSAGE;
                break;
            case CANCELLED:
                return EventConstant.LOBBY_CANCELED_MESSAGE.getText();
            case FINISHED:
                header = EventConstant.LOBBY_FINISHED_MESSAGE;
                break;
            default:
                header = EventConstant.LOBBY_PLAYING_MESSAGE;
                break;
        }
        return header.getText() + event.getPlayers().values()
                .stream()
                .map(KickerPlayer::getName)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private static String buildCheckingText(KickerEvent event) {
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

    private static String buildStatText(KickerEvent event) {
        return EventConstant.STAT_MESSAGE.getText();
    }
}
