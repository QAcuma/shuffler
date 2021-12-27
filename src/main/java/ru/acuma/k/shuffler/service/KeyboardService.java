package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface KeyboardService {

    InlineKeyboardMarkup getKeyboard(Long chatId);

}
