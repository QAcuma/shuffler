package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.acuma.shuffler.model.constant.Command;
import ru.acuma.shuffler.model.constant.keyboards.CallbackParam;
import ru.acuma.shuffler.model.constant.keyboards.CustomButton;
import ru.acuma.shuffler.model.constant.keyboards.KeyboardButton;
import ru.acuma.shuffler.model.constant.keyboards.Keyboards;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.model.domain.TMenu;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ButtonService {

    public InlineKeyboardButton getSingleButton(final Command command, final String text) {
        return InlineKeyboardButton.builder()
            .callbackData(command.getName())
            .text(text)
            .build();
    }

    public List<KeyboardButton> buildMenuButtons(final TMenu menu) {
        return switch (menu.getCurrentScreen()) {
            case MAIN -> Keyboards.MENU_DISCIPLINE_BUTTONS;
        };
    }

    public List<KeyboardButton> buildTimerButtons(final Integer time) {
        return switch (time) {
            case 3 -> Keyboards.IDLE_3_BUTTONS;
            case 2 -> Keyboards.IDLE_2_BUTTONS;
            case 1 -> Keyboards.IDLE_1_BUTTONS;
            default -> Keyboards.CHECKING_BUTTONS;
        };
    }

    public List<KeyboardButton> buildLobbyButtons(final TEvent event) {
        var eventState = event.getEventStatus();

        return switch (eventState) {
            case CREATED -> Keyboards.LOBBY_BUTTONS;
            case READY -> Keyboards.LOBBY_READY_BUTTONS;
            case CANCEL_CHECKING, FINISH_CHECKING, BEGIN_CHECKING, EVICTING -> Keyboards.CHECKING_BUTTONS;
            case GAME_CHECKING, ANY, CANCELLED, FINISHED -> Collections.emptyList();
            case PLAYING, WAITING_WITH_GAME, WAITING -> Keyboards.LOBBY_PLAYING_BUTTONS;
        };
    }

    public List<KeyboardButton> buildGameButtons(final TGame game) {
        return switch (game.getStatus()) {
            case ACTIVE -> Keyboards.GAME_BUTTONS;
            case EVICT_CHECKING -> buildEvictButtons(game);
            case RED_CHECKING, BLUE_CHECKING, CANCEL_CHECKING -> Keyboards.CHECKING_BUTTONS;
            default -> Collections.emptyList();
        };
    }

    public List<KeyboardButton> buildEvictButtons(final TGame game) {
        var players = game.getPlayers();
        var playerButtons = players.stream()
            .map(player -> CustomButton.builder()
                .text(player.getName())
                .callback(buildCommandCallback(Command.EVICT, CallbackParam.USER_ID, String.valueOf(player.getUserId())))
                .row(players.indexOf(player) + 1)
                .build()
            )
            .toList();
        var cancelButton = CustomButton.builder()
            .text("Не исключать")
            .callback(Command.CANCEL_EVICT.getName())
            .row(game.getPlayers().size() + 1)
            .build();

        return Stream.concat(
                playerButtons.stream(),
                Stream.<KeyboardButton>of(cancelButton))
            .toList();
    }

    private String buildCommandCallback(
        final Command command,
        final String param,
        final String identifier
    ) {
        return command.getName() + "?" + param + "=" + identifier;
    }
}
