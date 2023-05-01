package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.entity.Event;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.Discipline;

import java.time.LocalDateTime;

@Mapper(
    config = MapperConfiguration.class
)
public abstract class EventMapper {

    public TEvent initEvent(final Long chatId, final Discipline discipline) {
        return TEvent.builder()
            .eventStatus(EventStatus.CREATED)
            .chatId(chatId)
            .startedAt(LocalDateTime.now())
            .discipline(discipline)
            .build();
    }

    @Mapping(target = "chatId", source = "event.chatId")
    @Mapping(target = "seasonId", source = "seasonId")
    @Mapping(target = "state", source = "event.eventStatus")
    @Mapping(target = "discipline", source = "event.discipline")
    @Mapping(target = "season", ignore = true)
    @Mapping(target = "chat", ignore = true)
    public abstract Event toEvent(TEvent event, Long seasonId);

}
