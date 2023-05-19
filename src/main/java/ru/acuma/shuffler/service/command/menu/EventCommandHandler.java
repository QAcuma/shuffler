package ru.acuma.shuffler.service.command.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.context.Render;
import ru.acuma.shuffler.controller.EventCommand;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.keyboards.CallbackParam;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.service.command.common.BaseCommandHandler;
import ru.acuma.shuffler.util.ArgumentUtil;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class EventCommandHandler extends BaseCommandHandler<EventCommand> {

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
        var discipline = ArgumentUtil.extractParam(CallbackParam.DISCIPLINE, Function.identity(), args);
        beginEvent(message.getChatId(), Discipline.getByName(discipline));
    }

    private void beginEvent(final Long chatId, final Discipline discipline) {
        eventContext.createEvent(chatId, discipline);
        renderContext.forChat(chatId)
            .render(Render.forSend(MessageType.LOBBY).withAfterAction(
                () -> Render.forPin(renderContext.forChat(chatId).getMessageId(MessageType.LOBBY))
            ));
        renderContext.forChat(chatId).render(Render.forDelete(MessageType.MENU));
    }
}
