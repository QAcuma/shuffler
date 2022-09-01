package ru.acuma.shuffler.util;

import org.apache.commons.lang3.StringUtils;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.entity.TgEventPlayer;
import ru.acuma.shuffler.model.entity.TgGame;
import ru.acuma.shuffler.model.enums.messages.EventConstant;
import ru.acuma.shuffler.model.enums.messages.MessageType;

import java.util.Comparator;
import java.util.stream.Collectors;

import static ru.acuma.shuffler.model.enums.EventState.WAITING;
import static ru.acuma.shuffler.model.enums.GameState.FINISHED;
import static ru.acuma.shuffler.model.enums.messages.EventConstant.LET_JOIN_TEXT;
import static ru.acuma.shuffler.model.enums.messages.EventConstant.MEMBERS_TEXT;
import static ru.acuma.shuffler.util.Symbols.BLUE_CIRCLE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.RED_CIRCLE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.VS_EMOJI;

/**
 * Реализовать в качестве сервиса
 */
@Deprecated
public final class BuildMessageUtil {

    private BuildMessageUtil() {
    }

    public static String buildText(TgEvent event, MessageType type) {
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

    private static String buildGameText(TgEvent event) {
        var game = event.getCurrentGame();
        if (game == null) {
            return EventConstant.BLANK_MESSAGE.getText();
        }

        StringBuilder builder = new StringBuilder();

        builder
                .append(EventConstant.GAME_MESSAGE.getText())
                .append(game.getIndex())
                .append(EventConstant.SPACE_MESSAGE.getText())
                .append(System.lineSeparator());

        switch (game.getState()) {
            case STARTED:
            case CHECKING:
                buildStartedMessage(event, builder);
                break;
        }
        return builder.toString();
    }

    private static void buildStartedMessage(TgEvent event, StringBuilder builder) {
        var game = event.getCurrentGame();
        String spaces = getSpaces(game);
        builder
                .append(spaces)
//                .append(game.getRedTeam().getBetText())
                .append(System.lineSeparator())
                .append(String.format(game.getRedTeam().toString(), RED_CIRCLE_EMOJI))
                .append(System.lineSeparator())
                .append(spaces)
                .append(VS_EMOJI)
                .append(System.lineSeparator())
                .append(String.format(game.getBlueTeam().toString(), BLUE_CIRCLE_EMOJI))
                .append(System.lineSeparator())
                .append(spaces);
//                .append(game.getBlueTeam().getBetText());
    }

    private static String getSpaces(TgGame tgGame) {
        String spaces = tgGame.getRedTeam().getPlayer1().getName();
        return StringUtils.repeat(" ", spaces.length() * 2);
    }

    private static String buildLobbyText(TgEvent event) {
        StringBuilder builder = new StringBuilder();
        switch (event.getEventState()) {
            case CREATED:
            case READY:
            case CANCEL_LOBBY_CHECKING:
            case BEGIN_CHECKING:
                getMainEventMessage(event, builder);
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
        builder.append(event.getPlayers().isEmpty() ? LET_JOIN_TEXT.getText() : MEMBERS_TEXT.getText());
        builder.append(
                event.getPlayers().values()
                        .stream()
                        .sorted(Comparator.comparingLong(TgEventPlayer::getScoreSorting).reversed())
                        .map(TgEventPlayer::getLobbyName)
                        .collect(Collectors.joining(System.lineSeparator()))
        );
        builder.append(System.lineSeparator());
        buildResult(event, builder);
        if (event.getEventState() == WAITING) {
            builder
                    .append(System.lineSeparator())
                    .append(System.lineSeparator())
                    .append(EventConstant.WAITING_MESSAGE.getText())
                    .append(System.lineSeparator());
        }
        builder.append(EventConstant.SHUFFLER_LINK.getText());

        return builder.toString();
    }

    private static void getMainEventMessage(TgEvent event, StringBuilder builder) {
        switch (event.getDiscipline()) {
            case KICKER:
                builder.append(EventConstant.LOBBY_MESSAGE_KICKER.getText());
                break;
            case PING_PONG:
                builder.append(EventConstant.LOBBY_MESSAGE_PING_PONG.getText());
                break;
        }
    }

    private static void buildResult(TgEvent event, StringBuilder builder) {
        if (event.getTgGames().stream().anyMatch(game -> game.getState() == FINISHED)) {
            builder.append(System.lineSeparator())
                    .append(EventConstant.WINNERS_MESSAGE.getText());
        }

        event.getTgGames()
                .stream()
                .filter(game -> game.getWinnerTeam() != null)
                .forEach(game -> builder
                        .append(game.getGameResult())
                        .append(System.lineSeparator())
                );
    }

    private static String buildCheckingText(TgEvent event) {
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

    private static String buildStatText(TgEvent event) {
        return EventConstant.BLANK_MESSAGE.getText();
    }
}
