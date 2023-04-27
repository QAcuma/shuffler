package ru.acuma.shuffler.controller;

import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class BaseBotCommand {

    public abstract String getCommandIdentifier();

    public abstract void execute(Message message, String... args);
}
