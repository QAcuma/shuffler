package ru.acuma.shuffler.mapper;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import ru.acuma.shuffler.base.AbstractUnitTest;
import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.shuffler.model.enums.EventState;
import ru.acuma.shufflerlib.model.Discipline;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EventMapperTest extends AbstractUnitTest {

    @InjectMocks
    private EventMapper eventMapper;

    @Test
    void toGroupInfo() {
        GameEvent gameEvent = GameEvent.builder()
                .discipline(Discipline.KICKER_2VS2)
                .eventState(EventState.CREATED)
                .chatId(12345L)
                .startedAt(LocalDateTime.now())
                .build();

        var event = eventMapper.toEvent(gameEvent);
        assertAll(
                () -> assertEquals(event.getChatId(), gameEvent.getChatId()),
                () -> assertEquals(event.getDiscipline(), gameEvent.getDiscipline().name()),
                () -> assertEquals(event.getStartedAt().toLocalDateTime(), gameEvent.getStartedAt()),
                () -> assertEquals(event.getState(), gameEvent.getEventState().name())
        );
    }
}