package ru.acuma.shuffler.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.service.api.ExecuteService;

@Service
@RequiredArgsConstructor
public class RenderService {

    private final EventContext eventContext;
    private final ExecuteService executeService;

    public void render(Long chatId) {
        var event = eventContext.findEvent(chatId);
        event.getMessages();

    }

}
