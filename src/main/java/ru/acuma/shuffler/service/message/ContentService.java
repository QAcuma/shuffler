package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.Render;
import ru.acuma.shuffler.context.RenderContext;
import ru.acuma.shuffler.service.message.type.CancelledContent;
import ru.acuma.shuffler.service.message.type.CheckingContent;
import ru.acuma.shuffler.service.message.type.GameContent;
import ru.acuma.shuffler.service.message.type.LobbyContent;
import ru.acuma.shuffler.service.message.type.MenuContent;
import ru.acuma.shuffler.service.message.type.StatContent;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final RenderContext renderContext;
    private final MenuContent menuContent;
    private final LobbyContent lobbyContent;
    private final GameContent gameContent;
    private final CancelledContent cancelledContent;
    private final CheckingContent checkingContent;
    private final StatContent statContent;

    public void fillRenderContent(final Long chatId) {
        var chatRender = renderContext.forChat(chatId);
        chatRender.getRenders().values()
            .stream()
            .filter(Render::requireUpdate)
            .forEach(render -> fillContent(render, chatId));
    }

    private void fillContent(final Render render, final Long chatId) {
        switch (render.getMessageType()) {
            case MENU -> menuContent.fill(render, chatId);
            case LOBBY -> lobbyContent.fill(render, chatId);
            case GAME -> gameContent.fill(render, chatId);
            case CHECKING -> checkingContent.fill(render, chatId);
            case CANCELLED -> cancelledContent.fill(render, chatId);
            case STAT -> statContent.fill(render, chatId);
        }
    }
}
