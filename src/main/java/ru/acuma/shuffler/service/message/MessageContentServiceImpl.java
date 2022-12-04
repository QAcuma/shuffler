package ru.acuma.shuffler.service.message;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
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

@Service
public class MessageContentServiceImpl implements MessageContentService {

    @Override
    public String buildContent(TgEvent event, MessageType type) {
        return switch (type) {
            case LOBBY -> buildLobbyContent(event);
            case GAME -> buildGameContent(event);
            case CHECKING, CHECKING_TIMED -> buildCheckingContent(event);
            case CANCELLED -> buildCancelledContent(event);
            default -> EventConstant.BLANK_MESSAGE.getText();
        };
    }

    private String buildLobbyContent(TgEvent event) {
        var builder = new StringBuilder();
        return builder
                .append(applyEventHeader(event, builder))
                .append(event.getPlayers().isEmpty() ? LET_JOIN_TEXT.getText() : MEMBERS_TEXT.getText())
                .append(!event.getPlayers().isEmpty() ? getMembersText(event) : "")
                .append(event.hasAnyGameFinished() ? EventConstant.WINNERS_MESSAGE.getText() : "")
                .append(event.hasAnyGameFinished() ? buildWinnersText(event) : "")
                .append(event.getEventState() == WAITING ? EventConstant.WAITING_MESSAGE.getText() : "")
                .append(event.isCalibrating() ? EventConstant.CALIBRATING_MESSAGE.getText() : "")
                .append(EventConstant.SHUFFLER_LINK.getText())
                .toString();
    }

    private String applyEventHeader(TgEvent event, StringBuilder builder) {
        return switch (event.getEventState()) {
            case CREATED, READY, GAME_CHECKING, CANCEL_CHECKING, BEGIN_CHECKING -> getDisciplineHeader(event, builder);
            case WAITING -> EventConstant.LOBBY_WAITING_MESSAGE.getText();
            case CANCELLED -> EventConstant.LOBBY_CANCELED_MESSAGE.getText();
            case PLAYING -> EventConstant.LOBBY_PLAYING_MESSAGE.getText();
            case FINISHED -> EventConstant.LOBBY_FINISHED_MESSAGE.getText();
            default -> EventConstant.BLANK_MESSAGE.getText();
        };
    }

    private String getMembersText(TgEvent event) {
        return event.getPlayers().values()
                .stream()
                .sorted(Comparator.comparingLong(TgEventPlayer::getScoreSorting).reversed())
                .map(TgEventPlayer::getLobbyName)
                .collect(Collectors.joining(System.lineSeparator())) +
                System.lineSeparator();
    }

    private String buildWinnersText(TgEvent event) {
        return event.getTgGames()
                .stream()
                .filter(game -> game.getWinnerTeam() != null)
                .map(TgGame::getGameResult)
                .collect(Collectors.joining(System.lineSeparator())) +
                System.lineSeparator();
    }

    @SuppressWarnings("UnnecessaryDefault")
    private String getDisciplineHeader(TgEvent event, StringBuilder builder) {
        return switch (event.getDiscipline()) {
            case KICKER -> EventConstant.LOBBY_MESSAGE_KICKER.getText();
            case PING_PONG -> EventConstant.LOBBY_MESSAGE_PING_PONG.getText();
            default -> EventConstant.LOBBY_MESSAGE_DEFAULT.getText();
        };
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private String buildGameContent(TgEvent event) {
        var game = event.getLatestGame();
        var builder = new StringBuilder();

        return builder.append(EventConstant.GAME_MESSAGE.getText())
                .append(game.getIndex())
                .append(EventConstant.SPACE_MESSAGE.getText())
                .append(System.lineSeparator())
                .append(buildGameText(game))
                .toString();
    }

    private String buildGameText(TgGame game) {
        var builder = new StringBuilder();
        var spaces = getSpaces(game);

        return builder
                .append(spaces)
                .append(System.lineSeparator())
                .append(String.format(game.getRedTeam().toString(), RED_CIRCLE_EMOJI))
                .append(System.lineSeparator())
                .append(spaces)
                .append(VS_EMOJI)
                .append(System.lineSeparator())
                .append(String.format(game.getBlueTeam().toString(), BLUE_CIRCLE_EMOJI))
                .append(System.lineSeparator())
                .append(spaces)
                .toString();
    }

    private String getSpaces(TgGame tgGame) {
        String spaces = tgGame.getRedTeam().getPlayer1().getName();
        return StringUtils.repeat(" ", spaces.length() * 2);
    }

    private String buildCheckingContent(TgEvent event) {
        return switch (event.getEventState()) {
            case CANCEL_CHECKING -> EventConstant.CANCEL_CHECKING_MESSAGE.getText();
            case BEGIN_CHECKING -> EventConstant.BEGIN_CHECKING_MESSAGE.getText();
            case FINISH_CHECKING -> EventConstant.FINISH_CHECKING_MESSAGE.getText();
            case GAME_CHECKING -> buildGameCheckingText(event.getLatestGame());
            default -> EventConstant.BLANK_MESSAGE.getText();
        };
    }

    private String buildGameCheckingText(TgGame game) {
        return switch (game.getState()) {
            case RED_CHECKING -> EventConstant.RED_CHECKING_MESSAGE.getText();
            case BLUE_CHECKING -> EventConstant.BLUE_CHECKING_MESSAGE.getText();
            case CANCEL_CHECKING -> EventConstant.NEXT_CHECKING_MESSAGE.getText();
            default -> EventConstant.BLANK_MESSAGE.getText();
        };
    }

    private String buildCancelledContent(TgEvent event) {
        return EventConstant.LOBBY_CANCELED_MESSAGE.getText();
    }
}
