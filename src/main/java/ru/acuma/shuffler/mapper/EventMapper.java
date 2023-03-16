package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.dto.TgEvent;
import ru.acuma.shuffler.model.entity.Event;

@Mapper(
    config = MapperConfiguration.class
)
public abstract class EventMapper {

    @Mapping(source = "eventState", target = "state")
    @Mapping(target = "season", ignore = true)
    @Mapping(target = "chat", ignore = true)
    public abstract Event toEvent(TgEvent source);

}
