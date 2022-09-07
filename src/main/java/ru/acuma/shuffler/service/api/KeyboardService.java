package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.model.enums.messages.MessageType;

import java.util.Map;

public interface KeyboardService {

    InlineKeyboardMarkup getKeyboard(TgEvent event, MessageType type);

    InlineKeyboardMarkup getEmptyKeyboard();

    InlineKeyboardMarkup getTimedKeyboard(int time);

    InlineKeyboardMarkup getDynamicListKeyboard(Command command, Map<Long, String> map, InlineKeyboardButton... buttons);

    InlineKeyboardButton getSingleButton(Command command, String text);
}
