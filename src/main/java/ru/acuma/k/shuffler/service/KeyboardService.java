package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.enums.messages.MessageType;

public interface KeyboardService {

    InlineKeyboardMarkup getKeyboard(KickerEvent event, MessageType type);

    InlineKeyboardMarkup getEmptyKeyboard();

    InlineKeyboardMarkup getTimedKeyboard(int time);

}
