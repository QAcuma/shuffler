package ru.acuma.shuffler.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.acuma.shuffler.bot.ShufflerBot;
import ru.acuma.shuffler.context.cotainer.Render;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecuteService {

    private static final int TOO_MANY_REQUESTS_CODE = 429;
    private static final int BAD_REQUEST_CODE = 400;
    private static final String TIMEOUT_REGEX = "(?<=after )\\d+$";
    private static final Pattern pattern = Pattern.compile(TIMEOUT_REGEX);

    private final ExecutorService syncExecutors = Executors.newFixedThreadPool(4);
    private final ScheduledExecutorService scheduledExecutors = Executors.newScheduledThreadPool(4);

    private final ShufflerBot shufflerBot;

    @SneakyThrows
    public <T extends Serializable, M extends BotApiMethod<T>> T executeApi(M method) {
        return shufflerBot.executeApiMethod(method);
    }

    @SneakyThrows
    public <T extends Serializable, M extends BotApiMethod<T>> T execute(final M method, final Render render) {
        var result = syncExecutors.submit(() -> doExecute(method)).get();

        Optional.ofNullable(result)
            .filter(Message.class::isInstance)
            .map(Message.class::cast)
            .ifPresent(message -> render.setMessageId(message.getMessageId()));
        render.success();

        return result;
    }

    public <T extends Serializable, M extends BotApiMethod<T>> ScheduledFuture<T> executeLater(final M method, final Render render) {
        return scheduledExecutors.schedule(() -> execute(method, render), render.getDelay(), TimeUnit.SECONDS);
    }

    @SneakyThrows
    private <T extends Serializable, M extends BotApiMethod<T>> T doExecute(M method) {
        try {
            return shufflerBot.execute(method);
        } catch (TelegramApiRequestException e) {
            return switch (e.getErrorCode()) {
                case TOO_MANY_REQUESTS_CODE -> repeatLater(method, e);
                case BAD_REQUEST_CODE -> {
                    log.error(e.getMessage());
                    yield null;
                }
                default -> syncExecutors.submit(() -> shufflerBot.execute(method)).get();
            };
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private <T extends Serializable, M extends BotApiMethod<T>> T repeatLater(
        final M method,
        final TelegramApiRequestException e
    ) throws InterruptedException, ExecutionException {
        log.warn(e.getMessage());
        var matcher = pattern.matcher(e.getMessage());
        var delay = matcher.find() ? Long.parseLong(matcher.group(0)) : 30_000L;

        return scheduledExecutors.schedule(() -> doExecute(method), delay, TimeUnit.SECONDS).get();
    }
}
