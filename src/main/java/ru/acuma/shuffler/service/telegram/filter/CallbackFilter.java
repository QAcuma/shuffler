package ru.acuma.shuffler.service.telegram.filter;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.function.Consumer;

public interface CallbackFilter extends Consumer<CallbackQuery> {

    void accept(CallbackQuery callbackQuery);

}
