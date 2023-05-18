package ru.acuma.shuffler.service.message.type;

import ru.acuma.shuffler.context.Render;

public interface WithKeyboard<T> {

    void fillKeyboard(Render render, T subject);
}
