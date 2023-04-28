package ru.acuma.shuffler.service.message;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.constant.Command;

import java.util.Map;

public interface KeyboardService {

    InlineKeyboardMarkup getLobbyKeyboard(TEvent event);

    InlineKeyboardMarkup getCheckingKeyboard(TEvent event);

    InlineKeyboardMarkup getGamingKeyboard(TEvent event);

    InlineKeyboardMarkup getTimedKeyboard(int time);

    InlineKeyboardMarkup getEmptyKeyboard();

    InlineKeyboardMarkup getDynamicListKeyboard(Command command, Map<Long, String> map, InlineKeyboardButton... buttons);

    InlineKeyboardButton getSingleButton(Command command, String text);
}
