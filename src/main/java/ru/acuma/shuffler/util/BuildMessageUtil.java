package ru.acuma.shuffler.util;

import org.apache.commons.lang3.StringUtils;
import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.shuffler.model.entity.GameEventPlayer;
import ru.acuma.shuffler.model.entity.Game;
import ru.acuma.shuffler.model.enums.messages.EventConstant;
import ru.acuma.shuffler.model.enums.messages.MessageType;

import java.util.Comparator;
import java.util.stream.Collectors;

import static ru.acuma.shuffler.model.enums.EventState.WAITING;
import static ru.acuma.shuffler.model.enums.GameState.FINISHED;

public final class BuildMessageUtil {

    private BuildMessageUtil() {
    }

    public static String buildText(GameEvent event, MessageType type) {
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
                return EventConstant.BLANK_MESSAGE.getText();
        }
    }

    private static String buildGameText(GameEvent event) {
        var game = event.getCurrentGame();
        if (game == null) {
            return EventConstant.BLANK_MESSAGE.getText();
        }

        StringBuilder builder = new StringBuilder();

        builder
                .append(EventConstant.GAME_MESSAGE.getText())
                .append(game.getIndex())
                .append(System.lineSeparator())
                .append(EventConstant.SPACE_MESSAGE.getText());

        switch (game.getState()) {
            case STARTED:
            case CHECKING:
                buildStartedMessage(event, builder);
                break;
        }
        return builder.toString();
    }

    private static void buildStartedMessage(GameEvent event, StringBuilder builder) {
        var game = event.getCurrentGame();
        String spaces = getSpaces(game);
        builder
                .append(spaces)
                .append(game.getRedTeam().getRating())
                .append(System.lineSeparator())
                .append("⚔️ ")
                .append(String.format(game.getRedTeam().toString(), "\uD83D\uDD3A"))
                .append(" \uD83D\uDEE1")
                .append(System.lineSeparator())
                .append(spaces)
                .append("\uD83D\uDDEF\uD83D\uDDEF\uD83D\uDDEF")
                .append(System.lineSeparator())
                .append("⚔️ ")
                .append(String.format(game.getBlueTeam().toString(), "\uD83D\uDD39"))
                .append(" \uD83D\uDEE1")
                .append(System.lineSeparator())
                .append(spaces)
                .append(game.getBlueTeam().getRating())
                .append(System.lineSeparator());
    }

    private static String getSpaces(Game game) {
        String spaces = game.getRedTeam().getPlayer1().getName();
        return StringUtils.repeat(" ", spaces.length() * 2);
    }

    private static String buildLobbyText(GameEvent event) {
        StringBuilder builder = new StringBuilder();
        switch (event.getEventState()) {
            case CREATED:
            case READY:
            case CANCEL_LOBBY_CHECKING:
            case BEGIN_CHECKING:
                builder.append(EventConstant.LOBBY_MESSAGE.getText());
                break;
            case WAITING:
                builder.append(EventConstant.LOBBY_WAITING_MESSAGE.getText());
                break;
            case CANCELLED:
                return EventConstant.LOBBY_CANCELED_MESSAGE.getText();
            case PLAYING:
            case RED_CHECKING:
            case BLUE_CHECKING:
            case CANCEL_GAME_CHECKING:
                builder.append(EventConstant.LOBBY_PLAYING_MESSAGE.getText());
                break;
            case FINISHED:
                builder.append(EventConstant.LOBBY_FINISHED_MESSAGE.getText());
                break;
            default:
                builder.append(EventConstant.BLANK_MESSAGE.getText());
                break;
        }
        builder.append(
                event.getPlayers().values()
                        .stream()
                        .sorted(Comparator.comparingLong(GameEventPlayer::getRating).reversed())
                        .map(GameEventPlayer::getLobbyName)
                        .collect(Collectors.joining(System.lineSeparator()))
        );
        buildResult(event, builder);
        if (event.getEventState() == WAITING) {
            builder
                    .append(System.lineSeparator())
                    .append(System.lineSeparator())
                    .append(EventConstant.WAITING_MESSAGE.getText())
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }

    private static void buildResult(GameEvent event, StringBuilder builder) {
        if (event.getGames().stream().anyMatch(game -> game.getState() == FINISHED)) {
            builder.append(System.lineSeparator())
                    .append(EventConstant.WINNERS_MESSAGE.getText());
        }

        event.getGames()
                .stream()
                .filter(game -> game.getWinnerTeam() != null)
                .forEach(game -> {
                    builder
                            .append(game.getGameResult())
                            .append(System.lineSeparator());
                });
    }

    private static String buildCheckingText(GameEvent event) {
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
            case FINISH_CHECKING:
                return EventConstant.FINISH_CHECKING_MESSAGE.getText();
            default:
                return EventConstant.BLANK_MESSAGE.getText();
        }
    }

    private static String buildStatText(GameEvent event) {
        return EventConstant.BLANK_MESSAGE.getText();
    }
}
