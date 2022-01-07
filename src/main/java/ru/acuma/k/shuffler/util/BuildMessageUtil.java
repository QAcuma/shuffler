package ru.acuma.k.shuffler.util;

import org.apache.commons.lang3.StringUtils;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerGame;
import ru.acuma.k.shuffler.model.enums.messages.EventConstant;
import ru.acuma.k.shuffler.model.enums.messages.MessageType;
import ru.acuma.k.shuffler.service.enums.EventConstantApi;

import java.util.Comparator;
import java.util.stream.Collectors;

import static ru.acuma.k.shuffler.model.enums.messages.EventConstant.BLANK_MESSAGE;
import static ru.acuma.k.shuffler.model.enums.messages.EventConstant.CANCELLED_GAME_MESSAGE;
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
                return BLANK_MESSAGE.getText();
        }
    }

    private static String buildGameText(KickerEvent event) {
        var game = event.getCurrentGame();
        if (game == null) {
            return BLANK_MESSAGE.getText();
        }

        StringBuilder builder = new StringBuilder();

        builder
                .append(GAME_MESSAGE.getText())
                .append(game.getIndex())
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        switch (game.getState()) {
            case STARTED:
                buildStartedMessage(game, builder);
                builder
                        .append(System.lineSeparator())
                        .append(STARTED_GAME_MESSAGE.getText());
                break;
            case CANCELLED:
                buildCanceledMessage(game, builder);
                builder
                        .append(System.lineSeparator())
                        .append(CANCELLED_GAME_MESSAGE.getText());
                break;
            case CHECKING:
                buildStartedMessage(game, builder);
                break;
            case FINISHED:
                buildFinishedMessage(game, builder);
        }
        return builder.toString();
    }

    private static void buildStartedMessage(KickerGame game, StringBuilder builder) {
        String spaces = getSpaces(game);
        String moreSpaces = spaces + "   ";
        builder
                .append(moreSpaces)
                .append(game.getRedTeam().getRating())
                .append(System.lineSeparator())
                .append("🔴")
                .append(game.getRedTeam().toString())
                .append("🔴")
                .append(System.lineSeparator())
                .append(spaces)
                .append("⚡️⚡️⚡️")
                .append(System.lineSeparator())
                .append("🔵")
                .append(game.getBlueTeam().toString())
                .append("🔵")
                .append(System.lineSeparator())
                .append(moreSpaces)
                .append(game.getBlueTeam().getRating());
    }

    private static void buildCanceledMessage(KickerGame game, StringBuilder builder) {
        builder.append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(CANCELLED_GAME_MESSAGE.getText());
    }

    private static void buildFinishedMessage(KickerGame game, StringBuilder builder) {
        builder.append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(FINISHED_GAME_MESSAGE.getText())
                .append(game.getWinnerTeam().toString())
                .append(game.getWinnerTeam().getRatingChange());
    }

    private static String getSpaces(KickerGame game) {
        String spaces = game.getRedTeam().getPlayer1().getName();
        return StringUtils.repeat(" ", spaces.length() * 2);
    }

    private static String buildLobbyText(KickerEvent event) {
        EventConstantApi header;
        switch (event.getEventState()) {
            case CREATED:
            case READY:
            case CANCEL_LOBBY_CHECKING:
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
                header = BLANK_MESSAGE;
                break;
        }
        return header.getText() + event.getPlayers().values()
                .stream()
                .sorted(Comparator.comparingLong(KickerEventPlayer::getRating).reversed())
                .map(KickerEventPlayer::getLobbyName)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private static String buildCheckingText(KickerEvent event) {
        switch (event.getEventState()) {
            case CANCEL_LOBBY_CHECKING:
                return EventConstant.CANCEL_CHECKING_MESSAGE.getText();
            case BEGIN_CHECKING:
                return EventConstant.BEGIN_CHECKING_MESSAGE.getText();
            case CANCEL_GAME_CHECKING:
                return EventConstant.NEXT_CHECKING_MESSAGE.getText();
            case RED_CHECKING:
                return EventConstant.RED_CHECKING_MESSAGE.getText();
            case BLUE_CHECKING:
                return EventConstant.BLUE_CHECKING_MESSAGE.getText();
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
