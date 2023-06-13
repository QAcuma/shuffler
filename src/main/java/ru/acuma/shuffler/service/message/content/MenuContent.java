package ru.acuma.shuffler.service.message.content;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.MenuContext;
import ru.acuma.shuffler.context.cotainer.Render;
import ru.acuma.shuffler.model.domain.TMenu;
import ru.acuma.shuffler.service.message.KeyboardService;
import ru.acuma.shuffler.service.message.MessageTextService;

@Service
@RequiredArgsConstructor
public class MenuContent implements Fillable, WithText<TMenu>, WithKeyboard<TMenu> {

    private final MenuContext menuContext;
    private final MessageTextService messageTextService;
    private final KeyboardService keyboardService;

    @Override
    public void fill(final Render render, final Long chatId) {
        var menu = menuContext.findMenu(chatId);
        fillKeyboard(render, menu);
        fillText(render, menu);
    }

    @Override
    public void fillKeyboard(final Render render, final TMenu menu) {
        var keyboard = keyboardService.getMenuKeyboard(menu);
        render.withKeyboard(keyboard);
    }

    @Override
    public void fillText(final Render render, final TMenu menu) {
        var text = messageTextService.buildMenuMessage(menu);
        render.withText(text);
    }
}
