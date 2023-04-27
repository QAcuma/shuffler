package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.model.entity.Event;
import ru.acuma.shuffler.model.constant.EventState;
import ru.acuma.shufflerlib.model.Discipline;

import java.time.LocalDateTime;

@Mapper(
    config = MapperConfiguration.class
)
public abstract class EventMapper {

    public TgEvent initEvent(Long chatId, Discipline discipline) {
        return TgEvent.builder()
            .eventState(EventState.CREATED)
            .chatId(chatId)
            .startedAt(LocalDateTime.now())
            .discipline(discipline)
            .build();
    }

    @Mapping(source = "eventState", target = "state")
    @Mapping(target = "season", ignore = true)
    @Mapping(target = "chat", ignore = true)
    public abstract Event toEvent(TgEvent source);

}
