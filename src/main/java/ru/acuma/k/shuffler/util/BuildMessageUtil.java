package ru.acuma.k.shuffler.util;

import org.springframework.beans.factory.annotation.Autowired;
import ru.acuma.k.shuffler.cache.EventHolder;
import ru.acuma.k.shuffler.model.domain.KickerEvent;
import ru.acuma.k.shuffler.model.enums.messages.EventConstant;
import ru.acuma.k.shuffler.service.enums.EventConstantApi;

import java.util.stream.Collectors;

public final class BuildMessageUtil {

    @Autowired
    private EventHolder eventHolder;

    private BuildMessageUtil() {
    }

    public static String buildCreatedMessage(KickerEvent event) {
        EventConstantApi header = EventConstant.LOBBY_MESSAGE;
        return header.getText() + event.getMembers().stream()
                .map(user -> user.getFirstName() +
                        " " +
                        user.getLastName())
                .collect(Collectors.joining(System.lineSeparator()));
    }


}
