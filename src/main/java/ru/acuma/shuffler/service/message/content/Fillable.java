package ru.acuma.shuffler.service.message.content;

import ru.acuma.shuffler.context.cotainer.Render;

@FunctionalInterface
public interface Fillable {

    void fill(final Render render, final Long chatId);
}
