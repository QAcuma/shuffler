package ru.acuma.shuffler.service.telegram.filter;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.function.Consumer;

public interface UpdateValidator extends Consumer<CallbackQuery> {

    void accept(CallbackQuery callbackQuery);

    void accept(Message message);

}
