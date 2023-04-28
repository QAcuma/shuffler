package ru.acuma.shuffler.controller;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface BaseBotCommand {

    String getCommandIdentifier();

    void execute(Message message, String... args);
}
