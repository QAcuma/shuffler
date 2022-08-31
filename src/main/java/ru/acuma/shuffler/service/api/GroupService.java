package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.objects.Chat;

public interface GroupService {

    boolean authenticate(Chat chat);

}
