package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.entity.Event;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shufflerlib.model.Discipline;

import java.time.LocalDateTime;

@Mapper(
    config = MapperConfiguration.class
)
public abstract class EventMapper {

    public TEvent initEvent(final Long chatId, Discipline discipline) {
        return TEvent.builder()
            .eventStatus(EventStatus.CREATED)
            .chatId(chatId)
            .startedAt(LocalDateTime.now())
            .discipline(discipline)
            .build();
    }

    @Mapping(source = "getEventStatus", target = "state")
    @Mapping(target = "season", ignore = true)
    @Mapping(target = "chat", ignore = true)
    public abstract Event toEvent(TEvent source);

}
