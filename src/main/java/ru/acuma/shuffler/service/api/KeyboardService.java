package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.Command;

import java.util.Map;

public interface KeyboardService {

    InlineKeyboardMarkup getLobbyKeyboard(TgEvent event);

    InlineKeyboardMarkup getCheckingKeyboard(TgEvent event);

    InlineKeyboardMarkup getGamingKeyboard(TgEvent event);

    InlineKeyboardMarkup getTimedKeyboard(int time);

    InlineKeyboardMarkup getEmptyKeyboard();

    InlineKeyboardMarkup getDynamicListKeyboard(Command command, Map<Long, String> map, InlineKeyboardButton... buttons);

    InlineKeyboardButton getSingleButton(Command command, String text);
}
