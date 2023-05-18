package ru.acuma.shuffler.service.message.type;

import ru.acuma.shuffler.context.Render;

public interface WithText<T> {

    void fillText(Render render, T subject);

}
