package ru.acuma.shuffler.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.constant.messages.MenuScreen;
import ru.acuma.shuffler.model.domain.TMenu;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuContext {
    private final Map<Long, TMenu> menuStorage = new ConcurrentHashMap<>();

    public TMenu chatMenu(final Long chatId) {
        var menu = TMenu.builder().currentScreen(MenuScreen.MAIN).build();
        menuStorage.put(chatId, menu);

        return menu;
    }

    public TMenu findMenu(final Long chatId) {
        return Optional.ofNullable(menuStorage.get(chatId))
            .orElseGet(() -> chatMenu(chatId));
    }
}
