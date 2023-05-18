package ru.acuma.shuffler.service.message.content;

import ru.acuma.shuffler.context.Render;

@FunctionalInterface
public interface Fillable {

    void fill(final Render render, final Long chatId);
}
