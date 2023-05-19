package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.acuma.shuffler.model.constant.Command;
import ru.acuma.shuffler.model.constant.keyboards.KeyboardButton;
import ru.acuma.shuffler.model.constant.keyboards.Keyboards;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TMenu;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    private final ButtonService buttonService;

    public InlineKeyboardMarkup getMenuKeyboard(final TMenu menu) {
        var menuButtons = buttonService.buildMenuButtons(menu);
        return buildKeyboard(menuButtons);
    }

    public InlineKeyboardMarkup getLobbyKeyboard(final TEvent event) {
        var lobbyButtons = buttonService.buildLobbyButtons(event);

        return buildKeyboard(lobbyButtons);
    }

    public InlineKeyboardMarkup getCheckingKeyboard(final Integer timer) {
        var buttons = timer > BigDecimal.ZERO.intValue()
                      ? buttonService.buildTimerButtons(timer)
                      : Keyboards.CHECKING_BUTTONS;

        return buildKeyboard(buttons);
    }

    public InlineKeyboardMarkup getCheckingKeyboard() {
        return getCheckingKeyboard(0);
    }

    public InlineKeyboardMarkup getWaitingKeyboard() {
        return buildKeyboard(Keyboards.IDLE_BUTTONS);
    }

    public InlineKeyboardMarkup getGamingKeyboard(final TEvent event) {
        var gameButtons = buttonService.buildGameButtons(event.getCurrentGame());

        return buildKeyboard(gameButtons);
    }

    private InlineKeyboardMarkup buildKeyboard(final List<KeyboardButton> buttons) {
        return buttons.isEmpty()
               ? new InlineKeyboardMarkup(Collections.emptyList())
               : buildKeyboardStructure(buttons);
    }

    private InlineKeyboardMarkup buildKeyboardStructure(final List<KeyboardButton> names) {
        var buttons = IntStream.rangeClosed(1, 5)
            .mapToObj(rowNum -> mapToRow(names, rowNum))
            .toList();

        return InlineKeyboardMarkup.builder()
            .keyboard(buttons)
            .build();
    }

    private List<InlineKeyboardButton> mapToRow(final List<KeyboardButton> names, final int row) {
        return names.stream()
            .filter(button -> button.getRow() == row)
            .map(button -> InlineKeyboardButton.builder()
                .text(button.getText())
                .callbackData(button.getCallback())
                .build())
            .toList();
    }

    public InlineKeyboardButton getSingleButton(final Command command, final String text) {
        return InlineKeyboardButton.builder()
            .callbackData(command.getName())
            .text(text)
            .build();
    }
}
