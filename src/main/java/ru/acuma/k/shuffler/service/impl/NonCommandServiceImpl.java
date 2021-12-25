package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.model.enums.ConstantReplies;
import ru.acuma.k.shuffler.service.MessageService;
import ru.acuma.k.shuffler.service.MessageWayService;
import ru.acuma.k.shuffler.service.NonCommandService;
import ru.acuma.k.shuffler.service.UserService;

import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
public class NonCommandServiceImpl implements NonCommandService {

    private final MessageService messageService;
    private final MessageWayService messageWayService;
    private final UserService userService;

    /**
     * Формирование имени пользователя
     *
     * @param msg сообщение
     */
    private String getUserName(Message msg) {
        User user = msg.getFrom();
        return Objects.isNull(user.getFirstName()) ? user.getFirstName() : user.getUserName();
    }

    /**
     * Отправка ответа
     *
     * @param chatId id чата
     * @param text   текст ответа
     */
    private SendMessage setAnswer(Long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId.toString());
        answer.setText(text);
        return answer;
    }

    @Override
    public SendMessage process(Update update) {
        Message message = messageWayService.getMessage(update);
        if (Objects.isNull(message)) {
            return null;
        }
        if (!userService.validate(message.getFrom())) {
            return setAnswer(message.getChatId(), ConstantReplies.SERVICE_UNAVAILABLE.getValue());
        }
        return setAnswer(message.getChatId(), ConstantReplies.PONG.getValue());
    }
}
