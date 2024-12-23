package ru.acuma.shuffler.service.message;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.constant.messages.EventConstant;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.model.domain.TMenu;
import ru.acuma.shuffler.model.domain.TUserInfo;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.acuma.shuffler.model.constant.messages.EventConstant.EVICT_MESSAGE_TEXT;
import static ru.acuma.shuffler.model.constant.messages.EventConstant.LOBBY_MEMBERS_TEXT;
import static ru.acuma.shuffler.model.constant.messages.EventConstant.LOBBY_NO_MEMBERS_TEXT;
import static ru.acuma.shuffler.model.constant.messages.EventConstant.SINGLE_SPACE;
import static ru.acuma.shuffler.util.Symbols.BLUE_CIRCLE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.RED_CIRCLE_EMOJI;
import static ru.acuma.shuffler.util.Symbols.VS_EMOJI;

@Service
public class MessageTextService {

    @Value("${application.statistics.url}")
    private String shufflerHost;

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
            + (event.getPlayers().isEmpty() ? LOBBY_NO_MEMBERS_TEXT.getText() : LOBBY_MEMBERS_TEXT.getText())
            + (!event.getPlayers().isEmpty() ? getMembersText(event) : "")
            + (Boolean.TRUE.equals(event.hasAnyGameFinished()) ? EventConstant.LOBBY_WINNERS_MESSAGE.getText() : "")
            + (Boolean.TRUE.equals(event.hasAnyGameFinished()) ? getWinnersText(event) : "")
            + getEventFooterText(event);
    }

    public String buildGameContent(final TEvent event) {
        var game = event.getCurrentGame();
        return EventConstant.GAME_TITLE_MESSAGE.getText()
            + game.getOrder()
            + EventConstant.SPACE_MESSAGE.getText()
            + System.lineSeparator()
            + buildGameText(game);
    }

    public String buildCallText(final TGame game) {
        return game.getPlayers().stream()
            .filter(player -> !Objects.equals(player.getUserId(), game.getCalledBy()))
            .map(TEventPlayer::getUserInfo)
            .map(TUserInfo::getUserName)
            .map(name -> "@" + name)
            .collect(Collectors.joining(", "));
    }

    public String buildCancelledContent() {
        return EventConstant.LOBBY_CANCELED_MESSAGE.getText();
    }

    public String buildBlankContent() {
        return EventConstant.BLANK_MESSAGE.getText();
    }

    private String getEventFooterText(final TEvent event) {
        return switch (event.getEventStatus()) {
            case CANCEL_CHECKING -> EventConstant.LOBBY_CANCEL_CHECKING_MESSAGE.getText();
            case BEGIN_CHECKING -> EventConstant.GAME_BEGIN_CHECKING_MESSAGE.getText();
            case FINISH_CHECKING -> EventConstant.LOBBY_FINISH_CHECKING_MESSAGE.getText();
            default -> getStatisticsLink(event);
        };
    }

    private String getStatisticsLink(TEvent event) {
        // TODO: "/" + event.getChatName() +
        return "\n <i>" + shufflerHost +  "</i>";
    }

    private String getEventHeader(final TEvent event) {
        return switch (event.getEventStatus()) {
            case CREATED, READY, CANCEL_CHECKING, BEGIN_CHECKING -> getDisciplineHeader(event);
            case WAITING -> EventConstant.LOBBY_WAITING_MESSAGE.getText();
            case CANCELLED -> EventConstant.LOBBY_CANCELED_MESSAGE.getText();
            case PLAYING, WAITING_WITH_GAME, GAME_CHECKING, FINISH_CHECKING -> EventConstant.LOBBY_PLAYING_MESSAGE.getText();
            case FINISHED -> EventConstant.LOBBY_FINISHED_MESSAGE.getText();
            case ANY -> buildBlankContent();
        };
    }

    private String getMembersText(final TEvent event) {
        return event.getPlayers().values()
            .stream()
            .sorted(Comparator.comparingLong(TEventPlayer::getScore).reversed())
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

    private String getDisciplineHeader(final TEvent event) {
        return switch (event.getDiscipline()) {
            case KICKER -> EventConstant.LOBBY_MESSAGE_KICKER.getText();
            case PING_PONG -> EventConstant.LOBBY_MESSAGE_PING_PONG.getText();
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
            case RED_CHECKING -> EventConstant.GAME_RED_CHECKING_MESSAGE.getText();
            case BLUE_CHECKING -> EventConstant.GAME_BLUE_CHECKING_MESSAGE.getText();
            case CANCEL_CHECKING -> EventConstant.GAME_CANCEL_CHECKING_MESSAGE.getText();
            default -> getSpaces(game);
        };
    }

    private String getSpaces(final TGame tgGame) {
        String spaces = tgGame.getRedTeam().getPlayer1().getName();
        return StringUtils.repeat(" ", spaces.length() * 2);
    }
}
