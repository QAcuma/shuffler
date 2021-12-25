package ru.acuma.k.shuffler.service.impl;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.k.shuffler.service.MessageService;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public Message income(Message message) {

        return message;
    }


}
