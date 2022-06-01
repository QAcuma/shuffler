package ru.acuma.shuffler.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.acuma.shuffler.service.MessageWayService;

@Service
public class MessageWayServiceImpl implements MessageWayService {

    @Override
    public Message getMessage(Update update) {
        if (update.getMessage() != null) {
            return update.getMessage();
        }
        if (update.getEditedMessage() != null) {
            return update.getEditedMessage();
        }
        if (update.getChannelPost() != null) {
            return update.getEditedMessage();
        }
        if (update.getEditedChannelPost() != null) {
            return update.getEditedChannelPost();
        }
        return null;
    }


}
