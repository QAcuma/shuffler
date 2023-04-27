package ru.acuma.shuffler.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.acuma.shuffler.config.properties.BotProperties;
import ru.acuma.shuffler.service.telegram.EventRouter;

import java.io.Serializable;

public class ShufflerBot extends TelegramLongPollingBot {
    private final BotProperties botProperties;
    private final EventRouter commandRouter;

    public ShufflerBot(final BotProperties botProperties, final EventRouter commandRouter) {
        super(botProperties.getToken());
        this.botProperties = botProperties;
        this.commandRouter = commandRouter;
    }

    public final <T extends Serializable, M extends BotApiMethod<T>> T executeApiMethod(M method) throws TelegramApiException {
        return super.execute(method);
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public final void onUpdateReceived(Update update) {
        commandRouter.route(update);
    }
}
