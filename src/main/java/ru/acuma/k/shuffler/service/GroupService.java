package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.api.objects.Chat;

public interface GroupService {

    boolean authenticate(Chat chat);

}
