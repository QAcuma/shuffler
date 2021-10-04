package ru.acuma.trgb.service;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageService {

    Message income(Message message);

}
