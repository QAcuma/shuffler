package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.acuma.k.shuffler.model.entity.KickerEvent;

public interface KeyboardService {

    InlineKeyboardMarkup getKeyboard(KickerEvent event);

    InlineKeyboardMarkup getEmptyKeyboard();

    InlineKeyboardMarkup getTimedCheckingKeyboard(int time);

}
