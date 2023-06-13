package ru.acuma.shuffler.service.message.content;

import ru.acuma.shuffler.context.cotainer.Render;

public interface WithKeyboard<T> {

    void fillKeyboard(Render render, T subject);
}
