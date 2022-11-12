package ru.acuma.shuffler.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.acuma.shuffler.bot.ShufflerBot;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BasicBotConfig {

    private final ShufflerBot shufflerBot;

    @Bean
    @SneakyThrows
    public void connect() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(shufflerBot);
    }
}
