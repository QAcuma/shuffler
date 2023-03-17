package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.acuma.shuffler.model.domain.TgEvent;

import java.io.Serializable;

public interface ExecuteService {

    <T extends Serializable, M extends BotApiMethod<T>> T execute(M method);

    <T extends Serializable, M extends BotApiMethod<T>> void executeLater(M method, Long delay) throws TelegramApiException;

    <T extends Serializable, M extends BotApiMethod<T>> void executeRepeat(M method, TgEvent event);

    <T extends Serializable, M extends BotApiMethod<T>> T executeApi(M method) throws TelegramApiException;

}
