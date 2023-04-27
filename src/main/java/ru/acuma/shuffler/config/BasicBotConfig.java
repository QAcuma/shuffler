package ru.acuma.shuffler.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.acuma.shuffler.bot.ShufflerBot;
import ru.acuma.shuffler.config.properties.BotProperties;
import ru.acuma.shuffler.service.telegram.EventRouter;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class BasicBotConfig {

    @Lazy
    private final EventRouter commandRouter;

    @Bean
    @Validated
    @ConfigurationProperties(prefix = "telegram.bot")
    public BotProperties telegramBotProperties() {
        return new BotProperties();
    }

    @Bean
    @SneakyThrows
    public ShufflerBot shufflerBot(final BotProperties botProperties) {
        var shufflerBot = new ShufflerBot(botProperties, commandRouter);
        var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(shufflerBot);

        return shufflerBot;
    }
}
