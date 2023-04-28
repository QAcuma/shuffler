package ru.acuma.shuffler.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.acuma.shuffler.base.AbstractUnitTest;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shufflerlib.model.Discipline;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EventMapperTest extends AbstractUnitTest {

    @Autowired
    private EventMapper eventMapper;

    @Test
    void toEvent() {
        TEvent tgEvent = TEvent.builder()
                .discipline(Discipline.KICKER)
                .eventStatus(EventStatus.CREATED)
                .chatId(12345L)
                .startedAt(LocalDateTime.now())
                .build();

        var event = eventMapper.toEvent(tgEvent);
        assertAll(
                () -> assertEquals(event.getDiscipline(), tgEvent.getDiscipline().name()),
                () -> assertEquals(event.getStartedAt().toLocalDateTime(), tgEvent.getStartedAt()),
                () -> assertEquals(event.getState(), tgEvent.getEventStatus().name())
        );
    }
}
