package ru.acuma.shuffler.service.message.type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.Render;

@Service
@RequiredArgsConstructor
public class MenuContent implements Fillable {

    @Override
    public void fill(Render render, Long chatId) {

    }

}
