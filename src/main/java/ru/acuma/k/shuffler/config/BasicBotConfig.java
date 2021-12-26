package ru.acuma.k.shuffler.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.acuma.k.shuffler.bot.KickerBot;

@Slf4j
@Configuration
public class BasicBotConfig {

    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Bean
    public KickerBot botInit() {
        return new KickerBot(botName, botToken);
    }

    @Bean
    @SneakyThrows
    public void connect() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(botInit());
        } catch (TelegramApiException e) {
            log.error("Cannot initialize bot ", e);
        }
    }


}
