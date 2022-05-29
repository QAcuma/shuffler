package ru.acuma.k.shuffler.mapper;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import ru.acuma.k.shuffler.base.AbstractUnitTest;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.enums.EventState;
import ru.acuma.shufflerlib.model.Discipline;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EventMapperTest extends AbstractUnitTest {

    @InjectMocks
    private EventMapper eventMapper;

    @Test
    void toGroupInfo() {
        KickerEvent kickerEvent = KickerEvent.builder()
                .discipline(Discipline.KICKER_2VS2)
                .eventState(EventState.CREATED)
                .chatId(12345L)
                .startedAt(LocalDateTime.now())
                .build();

        var event = eventMapper.toEvent(kickerEvent);
        assertAll(
                () -> assertEquals(event.getChatId(), kickerEvent.getChatId()),
                () -> assertEquals(event.getDiscipline(), kickerEvent.getDiscipline().name()),
                () -> assertEquals(event.getStartedAt().toLocalDateTime(), kickerEvent.getStartedAt()),
                () -> assertEquals(event.getState(), kickerEvent.getEventState().name())
        );
    }
}