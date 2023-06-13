package ru.acuma.shuffler.service.message.content;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.cotainer.Render;

@Service
@RequiredArgsConstructor
public class StatContent implements Fillable {

    @Override
    public void fill(final Render render, final Long chatId) {

    }
}
