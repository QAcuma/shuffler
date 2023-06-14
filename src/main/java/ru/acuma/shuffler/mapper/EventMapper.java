package ru.acuma.shuffler.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.entity.Event;
import ru.acuma.shuffler.model.constant.EventStatus;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.entity.GroupInfo;
import ru.acuma.shuffler.model.entity.Season;

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

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "chat", source = "groupInfo")
    @Mapping(target = "season", source = "season")
    @Mapping(target = "status", source = "event.eventStatus")
    @Mapping(target = "discipline", source = "event.discipline")
    public abstract Event toEvent(TEvent event, GroupInfo groupInfo, Season season);
}
