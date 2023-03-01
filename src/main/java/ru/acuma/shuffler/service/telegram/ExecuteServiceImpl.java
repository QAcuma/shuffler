package ru.acuma.shuffler.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.acuma.shuffler.bot.ShufflerBot;
import ru.acuma.shuffler.cache.EventContext;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.Values;
import ru.acuma.shuffler.model.enums.messages.MessageType;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.service.api.MessageService;
import ru.acuma.shuffler.service.api.UserService;
import ru.acuma.shuffler.service.message.KeyboardService;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecuteServiceImpl implements ExecuteService {

    private static final int TOO_MANY_REQUESTS_CODE = 429;
    private static final int BAD_REQUEST_COE = 400;
    private static final String TIMEOUT_REGEX = "(?<=after )\\d+$";
    private static final Pattern pattern = Pattern.compile(TIMEOUT_REGEX);

    private final ExecutorService syncExecutors = Executors.newFixedThreadPool(4);
    private final ScheduledExecutorService asyncExecutors = Executors.newScheduledThreadPool(4);

    private final EventContext eventContext;
    private final @Lazy
    UserService userService;
    private final MessageService messageService;
    private final KeyboardService keyboardService;
    private final ShufflerBot shufflerBot;

    @Override
    @SneakyThrows
    public <T extends Serializable, M extends BotApiMethod<T>> T execute(M method) {
        var result = syncExecutors.submit(() -> doExecute(method)).get();

        Optional.ofNullable(result)
                .filter(Message.class::isInstance)
                .map(Message.class::cast)
                .ifPresent(message -> {
                    var event = eventContext.findEvent(message.getChatId());
                    Optional.ofNullable(event)
                            .ifPresent(activeEvent -> activeEvent.spyMessage(message.getMessageId()));
                });

        return result;
    }

    @Override
    public <T extends Serializable, M extends BotApiMethod<T>> void executeLater(M method, Long delay) {
        asyncExecutors.schedule(() -> doExecute(method), delay, TimeUnit.SECONDS);
    }

    @Override
    public <T extends Serializable, M extends BotApiMethod<T>> void executeRepeat(M method, TgEvent event) {
        var message = Optional.of(execute(method))
                .filter(Message.class::isInstance)
                .map(Message.class::cast)
                .orElseThrow();

        var update = messageService.buildReplyMarkupUpdate(
                event,
                message.getMessageId(),
                MessageType.CHECKING_TIMED
        );

        IntStream.rangeClosed(1, Values.TIMEOUT).forEach(delay -> {
            Runnable repeatableExecutor = () -> {
                update.setReplyMarkup(keyboardService.getTimedKeyboard(Values.TIMEOUT - delay));
                doExecute(update);
            };

            var futureExecutor = asyncExecutors.schedule(
                    repeatableExecutor,
                    delay,
                    TimeUnit.SECONDS
            );
            event.watchFuture(futureExecutor);
        });
    }

    @Override
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public <T extends Serializable, M extends BotApiMethod<T>> T executeApi(M method) {
        var userId = method instanceof GetUserProfilePhotos getPhotos
                ? getPhotos.getUserId()
                : 0L;
        T result = null;
        try {
            result = shufflerBot.executeApiMethod(method);
        } catch (TelegramApiException exception) {
            var reason = "";
            if (exception instanceof TelegramApiRequestException requestException) {
                reason = requestException.getApiResponse();
            }
            switch (reason) {
                case "Bad Request: user not found" -> userService.deleteUser(userId);
                default -> log.warn(reason);
            }
        }

        return result;
    }

    @SneakyThrows
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    private <T extends Serializable, M extends BotApiMethod<T>> T doExecute(M method) {
        try {
            return shufflerBot.execute(method);
        } catch (TelegramApiRequestException e) {
            log.warn(e.getMessage());

            return switch (e.getErrorCode()) {
                case TOO_MANY_REQUESTS_CODE -> repeatLater(method, e);
                default -> syncExecutors.submit(() -> shufflerBot.execute(method)).get();
            };
        }
    }

    private <T extends Serializable, M extends BotApiMethod<T>> T repeatLater(M method, TelegramApiRequestException e) throws InterruptedException, ExecutionException {
        var matcher = pattern.matcher(e.getMessage());
        var delay = matcher.find() ? Long.parseLong(matcher.group(0)) : 30_000L;

        return asyncExecutors.schedule(() -> doExecute(method), delay, TimeUnit.SECONDS).get();
    }

}
