package ru.acuma.shuffler.service.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.context.Render;
import ru.acuma.shuffler.controller.EventCommand;
import ru.acuma.shuffler.controller.MenuCommand;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.util.ArgumentUtil;

import java.util.List;

@Slf4j
@Service
public class MenuCommandHandler extends BaseCommandHandler<MenuCommand> {

    private static final String DISCIPLINE_PARAM = "discipline";

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
        var discipline = ArgumentUtil.extractParam(DISCIPLINE_PARAM, args);
        beginEvent(message.getChatId(), Discipline.getByWebName(discipline));
    }

    private void beginEvent(final Long chatId, final Discipline discipline) {
        var chatRender = renderContext.forChat(chatId);
        eventContext.createEvent(chatId, discipline);
        chatRender.render(Render.forSend(MessageType.LOBBY).withAfterAction(
            () -> Render.forPin(chatRender.getMessageId(MessageType.LOBBY))
        ));
    }
}
