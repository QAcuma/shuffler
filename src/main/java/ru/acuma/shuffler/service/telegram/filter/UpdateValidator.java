package ru.acuma.shuffler.service.telegram.filter;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface UpdateValidator {

    void accept(CallbackQuery callbackQuery);

    void accept(Message message);
}
