package ru.acuma.shuffler.service.command.menu;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.context.MenuContext;
import ru.acuma.shuffler.context.cotainer.Render;
import ru.acuma.shuffler.controller.MenuCommand;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.command.common.BaseCommandHandler;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuCommandHandler extends BaseCommandHandler<MenuCommand> {

    private final MenuContext menuContext;

    @Override
    protected List<EventStatus> getSupportedStatuses() {
        return List.of(EventStatus.ANY);
    }

    @Override
    public void invokeEventCommand(final User user, final TEvent event, final String... args) {
        log.debug("Event is present in chat %s".formatted(event.getChatId()));
    }

    @Override
    protected void invokeChatCommand(final Message message, final String... args) {
        showMenu(message.getChatId());
    }

    private void showMenu(final Long chatId) {
        var chatRender = renderContext.forChat(chatId);
        menuContext.newMenu(chatId);
        chatRender.render(Render.forSend(MessageType.MENU));
    }
}
