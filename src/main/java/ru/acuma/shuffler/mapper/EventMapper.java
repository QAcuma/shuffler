package ru.acuma.shuffler.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.entity.Event;
import ru.acuma.shuffler.model.entity.GroupInfo;
import ru.acuma.shuffler.model.entity.Season;
import ru.acuma.shuffler.util.TimeMachine;

@Mapper(
    config = MapperConfiguration.class,
    imports = TimeMachine.class
)
public interface EventMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "chatId", source = "chatId")
    @Mapping(target = "startedAt", expression = "java(TimeMachine.localDateTimeNow())")
    @Mapping(target = "eventStatus", constant = "CREATED")
    @Mapping(target = "discipline", source = "discipline")
    TEvent initEvent(final Long chatId, final Discipline discipline);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "chat", source = "groupInfo")
    @Mapping(target = "season", source = "season")
    @Mapping(target = "status", source = "event.eventStatus")
    @Mapping(target = "discipline", source = "event.discipline")
    Event toEvent(TEvent event, GroupInfo groupInfo, Season season);
}
