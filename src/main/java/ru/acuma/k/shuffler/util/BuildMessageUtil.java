package ru.acuma.k.shuffler.util;

import org.apache.commons.lang3.StringUtils;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerPlayer;
import ru.acuma.k.shuffler.model.enums.messages.EventConstant;
import ru.acuma.k.shuffler.model.enums.messages.MessageType;
import ru.acuma.k.shuffler.service.enums.EventConstantApi;

import java.util.stream.Collectors;

import static ru.acuma.k.shuffler.model.enums.messages.EventConstant.CANCELLED_GAME_MESSAGE;
import static ru.acuma.k.shuffler.model.enums.messages.EventConstant.DEFAULT_MESSAGE;
import static ru.acuma.k.shuffler.model.enums.messages.EventConstant.FINISHED_GAME_MESSAGE;
import static ru.acuma.k.shuffler.model.enums.messages.EventConstant.GAME_MESSAGE;
import static ru.acuma.k.shuffler.model.enums.messages.EventConstant.STARTED_GAME_MESSAGE;

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
        var game = event.getLastGame();
        if (game == null) {
            return DEFAULT_MESSAGE.getText();
        }

        StringBuilder builder = new StringBuilder();
        builder
                .append(System.lineSeparator())
                .append(GAME_MESSAGE.getText())
                .append(game.getIndex())
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(StringUtils.repeat("    ", 6))
                .append(game.getRedTeam().getRating())
                .append(System.lineSeparator())
                .append("♦️")
                .append(game.getRedTeam().toString())
                .append("♦️ ")
                .append(System.lineSeparator())
                .append(StringUtils.repeat("    ", 5))
                .append("⚡️⚡️⚡️")
                .append(System.lineSeparator())
                .append("\uD83D\uDD37")
                .append(game.getBlueTeam().toString())
                .append("\uD83D\uDD37 ")
                .append(System.lineSeparator())
                .append(StringUtils.repeat("    ", 6))
                .append(game.getBlueTeam().getRating());

        switch (game.getState()) {
            case STARTED:
                builder.append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(STARTED_GAME_MESSAGE.getText());
                break;
            case CANCELLED:
                builder.append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(CANCELLED_GAME_MESSAGE.getText());
                break;
            case FINISHED:
                builder.append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(FINISHED_GAME_MESSAGE.getText())
                        .append(game.getWinnerTeam().toString())
                        .append(game.getWinnerTeam().getRatingChange());
        }
        return builder.toString();
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
            case PLAYING:
                header = EventConstant.LOBBY_PLAYING_MESSAGE;
                break;
            case FINISHED:
                header = EventConstant.LOBBY_FINISHED_MESSAGE;
                break;
            default:
                header = DEFAULT_MESSAGE;
                break;
        }
        return header.getText() + event.getPlayers().values()
                .stream()
                .map(KickerEventPlayer::getName)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private static String buildCheckingText(KickerEvent event) {
        switch (event.getEventState()) {
            case CANCEL_CHECKING:
                return EventConstant.CANCEL_CHECKING_MESSAGE.getText();
            case BEGIN_CHECKING:
                return EventConstant.BEGIN_CHECKING_MESSAGE.getText();
            case NEXT_CHECKING:
                return EventConstant.NEXT_CHECKING_MESSAGE.getText();
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
