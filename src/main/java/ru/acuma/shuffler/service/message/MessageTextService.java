package ru.acuma.shuffler.service.message;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.constant.messages.EventConstant;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.model.domain.TMenu;

import java.util.Comparator;
import java.util.stream.Collectors;

import static ru.acuma.shuffler.model.constant.messages.EventConstant.EVICT_MESSAGE_TEXT;
import static ru.acuma.shuffler.model.constant.messages.EventConstant.LET_JOIN_TEXT;
import static ru.acuma.shuffler.model.constant.messages.EventConstant.MEMBERS_TEXT;
import static ru.acuma.shuffler.model.constant.messages.EventConstant.SHUFFLER_LINK;
import static ru.acuma.shuffler.model.constant.messages.EventConstant.SINGLE_SPACE;
import static ru.acuma.shuffler.util.Symbols.BLUE_CIRCLE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.RED_CIRCLE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.VS_EMOJI;

@Service
public class MessageTextService {

    public String buildMenuMessage(final TMenu menu) {
        return switch (menu.getCurrentScreen()) {
            case MAIN -> getMainMessage();
        };
    }

    private String getMainMessage() {
        return EventConstant.MENU_MAIN_MESSAGE.getText();
    }

    public String buildLobbyContent(final TEvent event) {
        return getEventHeader(event)
            + (event.getPlayers().isEmpty() ? LET_JOIN_TEXT.getText() : MEMBERS_TEXT.getText())
            + (!event.getPlayers().isEmpty() ? getMembersText(event) : "")
            + (Boolean.TRUE.equals(event.hasAnyGameFinished()) ? EventConstant.WINNERS_MESSAGE.getText() : "")
            + (Boolean.TRUE.equals(event.hasAnyGameFinished()) ? getWinnersText(event) : "")
            + (Boolean.TRUE.equals(event.isCalibrating()) ? EventConstant.CALIBRATING_MESSAGE.getText() : "")
            + getEventFooterText(event);
    }

    public String buildGameContent(final TEvent event) {
        var game = event.getCurrentGame();
        return EventConstant.GAME_MESSAGE.getText()
            + game.getOrder()
            + EventConstant.SPACE_MESSAGE.getText()
            + System.lineSeparator()
            + buildGameText(game);
    }

    public String buildCheckingContent(final TEvent event) {
        return switch (event.getEventStatus()) {
            case EVICTING -> EventConstant.EVICT_PLAYER_CHECKING.getText();
            default -> buildBlankContent();
        };
    }

    public String buildCancelledContent() {
        return EventConstant.LOBBY_CANCELED_MESSAGE.getText();
    }

    public String buildBlankContent() {
        return EventConstant.BLANK_MESSAGE.getText();
    }

    private String getEventFooterText(final TEvent event) {
        return switch (event.getEventStatus()) {
            case CANCEL_CHECKING -> EventConstant.CANCEL_CHECKING_MESSAGE.getText();
            case BEGIN_CHECKING -> EventConstant.BEGIN_CHECKING_MESSAGE.getText();
            case FINISH_CHECKING -> EventConstant.FINISH_CHECKING_MESSAGE.getText();
            default -> SHUFFLER_LINK.getText();
        };
    }

    private String getEventHeader(final TEvent event) {
        return switch (event.getEventStatus()) {
            case CREATED, READY, GAME_CHECKING, CANCEL_CHECKING, BEGIN_CHECKING -> getDisciplineHeader(event);
            case WAITING -> EventConstant.LOBBY_WAITING_MESSAGE.getText();
            case CANCELLED -> EventConstant.LOBBY_CANCELED_MESSAGE.getText();
            case PLAYING -> EventConstant.LOBBY_PLAYING_MESSAGE.getText();
            case FINISHED -> EventConstant.LOBBY_FINISHED_MESSAGE.getText();
            default -> buildBlankContent();
        };
    }

    private String getMembersText(final TEvent event) {
        return event.getPlayers().values()
            .stream()
            .sorted(Comparator.comparingLong(TEventPlayer::getScoreSorting).reversed())
            .map(TEventPlayer::getLobbyName)
            .collect(Collectors.joining(System.lineSeparator())) +
            System.lineSeparator();
    }

    private String getWinnersText(final TEvent event) {
        return event.getTgGames()
            .stream()
            .filter(game -> game.getWinnerTeam() != null)
            .map(TGame::getGameResult)
            .collect(Collectors.joining(System.lineSeparator())) +
            System.lineSeparator();
    }

    @SuppressWarnings("UnnecessaryDefault")
    private String getDisciplineHeader(final TEvent event) {
        return switch (event.getDiscipline()) {
            case KICKER -> EventConstant.LOBBY_MESSAGE_KICKER.getText();
            case PING_PONG -> EventConstant.LOBBY_MESSAGE_PING_PONG.getText();
            default -> EventConstant.LOBBY_MESSAGE_DEFAULT.getText();
        };
    }

    private String buildGameText(final TGame game) {
        return getGameBodyText(game)
            + EventConstant.SINGLE_SPACE.getText()
            + getGameFooterText(game);
    }

    private String getGameBodyText(final TGame game) {
        return switch (game.getStatus()) {
            case RED_CHECKING -> RED_CIRCLE_EMOJI + SINGLE_SPACE.getText() + getRedWinnersBody(game);
            case BLUE_CHECKING -> BLUE_CIRCLE_EMOJI + SINGLE_SPACE.getText() + getBlueWinnersBody(game);
            case EVICT_CHECKING -> EVICT_MESSAGE_TEXT.getText();
            case ACTIVE, CANCEL_CHECKING -> getDefaultGameBody(game);
            default -> buildBlankContent();
        };
    }

    private String getDefaultGameBody(final TGame game) {
        return String.format(game.getRedTeam().toString(), RED_CIRCLE_EMOJI) + System.lineSeparator()
            + getSpaces(game) + VS_EMOJI + System.lineSeparator()
            + String.format(game.getBlueTeam().toString(), BLUE_CIRCLE_EMOJI) + System.lineSeparator();
    }

    private String getRedWinnersBody(final TGame game) {
        return String.format(game.getRedTeam().toString(), "и");
    }

    private String getBlueWinnersBody(final TGame game) {
        return String.format(game.getBlueTeam().toString(), "и");
    }

    private String getGameFooterText(final TGame game) {
        return switch (game.getStatus()) {
            case RED_CHECKING, BLUE_CHECKING -> EventConstant.WIN_CHECKING_MESSAGE.getText();
            case CANCEL_CHECKING -> EventConstant.NEXT_CHECKING_MESSAGE.getText();
            default -> getSpaces(game);
        };
    }

    private String getSpaces(final TGame tgGame) {
        String spaces = tgGame.getRedTeam().getPlayer1().getName();
        return StringUtils.repeat(" ", spaces.length() * 2);
    }
}
