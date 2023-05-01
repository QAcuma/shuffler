package ru.acuma.shuffler.service.telegram;

import ru.acuma.shuffler.model.constant.AuthStatus;

public interface Authenticatable<T> {

    AuthStatus authenticate(T subject);

}
