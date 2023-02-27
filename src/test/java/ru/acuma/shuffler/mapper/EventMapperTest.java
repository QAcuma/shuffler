package ru.acuma.shuffler.mapper;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import ru.acuma.shuffler.base.AbstractUnitTest;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shufflerlib.model.Discipline;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EventMapperTest extends AbstractUnitTest {

    @Autowired
    private EventMapper eventMapper;

    @Test
    void toEvent() {
        TgEvent tgEvent = TgEvent.builder()
                .discipline(Discipline.KICKER)
                .eventState(EventState.CREATED)
                .chatId(12345L)
                .startedAt(LocalDateTime.now())
                .build();

        var event = eventMapper.toEvent(tgEvent);
        assertAll(
                () -> assertEquals(event.getChatId(), tgEvent.getChatId()),
                () -> assertEquals(event.getDiscipline(), tgEvent.getDiscipline().name()),
                () -> assertEquals(event.getStartedAt().toLocalDateTime(), tgEvent.getStartedAt()),
                () -> assertEquals(event.getState(), tgEvent.getEventState().name())
        );
    }
}