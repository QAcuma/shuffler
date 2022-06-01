package ru.acuma.shuffler.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.shuffler.model.enums.messages.MessageType;

public interface KeyboardService {

    InlineKeyboardMarkup getKeyboard(GameEvent event, MessageType type);

    InlineKeyboardMarkup getEmptyKeyboard();

    InlineKeyboardMarkup getTimedKeyboard(int time);

}
