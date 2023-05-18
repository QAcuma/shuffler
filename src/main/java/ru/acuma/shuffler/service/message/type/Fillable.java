package ru.acuma.shuffler.service.message.type;

import ru.acuma.shuffler.context.Render;

@FunctionalInterface
public interface Fillable {

    void fill(Render render, Long chatId);
}
