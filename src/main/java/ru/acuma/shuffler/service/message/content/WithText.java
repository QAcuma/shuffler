package ru.acuma.shuffler.service.message.content;

import ru.acuma.shuffler.context.cotainer.Render;

public interface WithText<T> {

    void fillText(Render render, T subject);

}
