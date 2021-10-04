package ru.acuma.trgb.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageWayService {

    Message getMessage(Update update);

}
