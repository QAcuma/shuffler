package ru.acuma.shuffler.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.acuma.shuffler.service.api.GroupService;
import ru.acuma.shuffler.service.api.NonCommandService;
import ru.acuma.shuffler.service.api.UserService;
import ru.acuma.shuffler.service.command.BaseBotCommand;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShufflerBot extends TelegramLongPollingCommandBot {

    @Value("${telegram.bot.name}")
    private String botName;

    @Value("${telegram.bot.token}")
    private String botToken;

    private final List<BaseBotCommand> commands;
    private final UserService userService;
    private final NonCommandService nonCommandService;
    private final GroupService groupService;

    @PostConstruct
    private void init() {
        commands.forEach(this::register);
    }

    @Override
    @SneakyThrows
    protected boolean filter(Message message) {
        var method = new GetUserProfilePhotos();
        method.setUserId(message.getFrom().getId());
        var photos = this.sendApiMethod(method);
        var fileMethod = new GetFile();

        photos.getPhotos().stream()
                .findFirst()
                .flatMap(photo -> photo.stream().findFirst())
                .ifPresent(smallPhoto -> {
                    fileMethod.setFileId(smallPhoto.getFileId());
                    loadUserProfilePicture(message.getFrom().getId(), fileMethod);
                });

        return groupService.authenticate(message.getChat()) && !userService.authenticate(message.getFrom());
    }

    @SneakyThrows
    private void loadUserProfilePicture(Long telegramId, GetFile fileMethod) {
        File photo = this.sendApiMethod(fileMethod);

        userService.saveProfilePhotos(telegramId, photo);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.getCallbackQuery() != null) {
            processCallback(update.getCallbackQuery());
        }
        reply(nonCommandService.process(update));
    }

    private void processCallback(CallbackQuery callbackQuery) {
        callbackQuery.getMessage().setFrom(callbackQuery.getFrom());
        if (filter(callbackQuery.getMessage())) {
            return;
        }
        String command = StringUtils.substringBefore(callbackQuery.getData(), "?");
        callbackQuery.getMessage().setText(StringUtils.substringAfter(callbackQuery.getData(), "?"));
        getRegisteredCommand(command).processMessage(
                this,
                callbackQuery.getMessage(),
                new String[]{}
        );
    }

    public <M extends SendMessage> void reply(M message) {
        if (Objects.isNull(message)) {
            return;
        }
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send answer", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
