package ru.acuma.k.shuffle.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface NonCommandService {

    SendMessage process(Update update);
}
