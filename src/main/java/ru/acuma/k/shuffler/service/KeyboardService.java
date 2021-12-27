package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.acuma.k.shuffler.model.enums.EventState;

public interface KeyboardService {

    InlineKeyboardMarkup getKeyboard(EventState eventState);

    InlineKeyboardMarkup getTimedCheckingKeyboard(int time);

}
