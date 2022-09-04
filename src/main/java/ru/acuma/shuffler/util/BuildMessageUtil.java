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
        var game = event.getLastGame();
        if (game == null) {
            return EventConstant.BLANK_MESSAGE.getText();
        }

        StringBuilder builder = new StringBuilder();

        builder.append(EventConstant.GAME_MESSAGE.getText())
                .append(game.getIndex())
                .append(EventConstant.SPACE_MESSAGE.getText())
                .append(System.lineSeparator());

        switch (game.getState()) {
            case ACTIVE:
            case BLUE_CHECKING:
            case RED_CHECKING:
            case CANCEL_CHECKING:
                buildActiveMessage(event, builder);
                break;
        }

        return builder.toString();
    }

    private static void buildActiveMessage(TgEvent event, StringBuilder builder) {
        var game = event.getLastGame();
        String spaces = getSpaces(game);
        builder
                .append(spaces)
                .append(System.lineSeparator())
                .append(String.format(game.getRedTeam().toString(), RED_CIRCLE_EMOJI))
                .append(System.lineSeparator())
                .append(spaces)
                .append(VS_EMOJI)
                .append(System.lineSeparator())
                .append(String.format(game.getBlueTeam().toString(), BLUE_CIRCLE_EMOJI))
                .append(System.lineSeparator())
                .append(spaces);
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
            case CANCEL_CHECKING:
            case BEGIN_CHECKING:
                getMainEventMessage(event, builder);
                break;
            case WAITING:
                builder.append(EventConstant.LOBBY_WAITING_MESSAGE.getText());
                break;
            case CANCELLED:
                return EventConstant.LOBBY_CANCELED_MESSAGE.getText();
            case PLAYING:
                builder.append(EventConstant.LOBBY_PLAYING_MESSAGE.getText());
                break;
            case FINISHED:
                builder.append(EventConstant.LOBBY_FINISHED_MESSAGE.getText());
                break;
            default:
                builder.append(EventConstant.BLANK_MESSAGE.getText());
                break;
        }

        return builder.append(event.getPlayers().isEmpty() ? LET_JOIN_TEXT.getText() : MEMBERS_TEXT.getText())
                .append(!event.getPlayers().isEmpty() ? getMembersText(event) : "")
                .append(event.hasAnyGameFinished() ? EventConstant.WINNERS_MESSAGE.getText() : "")
                .append(event.hasAnyGameFinished() ? buildWinnersText(event) : "")
                .append(event.getEventState() == WAITING ? EventConstant.WAITING_MESSAGE.getText() : "")
                .append(event.isCalibrating() ? EventConstant.CALIBRATING_MESSAGE.getText() : "")
                .append(EventConstant.SHUFFLER_LINK.getText())
                .toString();
    }

    private static String getMembersText(TgEvent event) {
        return event.getPlayers().values()
                .stream()
                .sorted(Comparator.comparingLong(TgEventPlayer::getScoreSorting).reversed())
                .map(TgEventPlayer::getLobbyName)
                .collect(Collectors.joining(System.lineSeparator())) +
                System.lineSeparator();
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

    private static String buildWinnersText(TgEvent event) {
        return event.getTgGames()
                .stream()
                .filter(game -> game.getWinnerTeam() != null)
                .map(TgGame::getGameResult)
                .collect(Collectors.joining(System.lineSeparator())) +
                System.lineSeparator();

    }

    private static String buildCheckingText(TgEvent event) {
        switch (event.getEventState()) {
            case CANCEL_CHECKING:
                return EventConstant.CANCEL_CHECKING_MESSAGE.getText();
            case BEGIN_CHECKING:
                return EventConstant.BEGIN_CHECKING_MESSAGE.getText();
            case FINISH_CHECKING:
                return EventConstant.FINISH_CHECKING_MESSAGE.getText();
            case PLAYING:
            case WAITING_WITH_GAME:
                return buildGameCheckingText(event.getLastGame());
            default:
                return EventConstant.BLANK_MESSAGE.getText();
        }
    }

    private static String buildGameCheckingText(TgGame currentGame) {
        switch (currentGame.getState()) {
            case RED_CHECKING:
                return EventConstant.RED_CHECKING_MESSAGE.getText();
            case BLUE_CHECKING:
                return EventConstant.BLUE_CHECKING_MESSAGE.getText();
            case CANCEL_CHECKING:
                return EventConstant.NEXT_CHECKING_MESSAGE.getText();
            default:
                return EventConstant.BLANK_MESSAGE.getText();
        }
    }

    private static String buildStatText(TgEvent event) {
        return EventConstant.BLANK_MESSAGE.getText();
    }
}
