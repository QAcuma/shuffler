package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.dto.TgEvent;
import ru.acuma.shuffler.tables.pojos.Event;

@Mapper(uses = OffsetDateTimeMapper.class, componentModel = "spring")
public abstract class EventMapper {

    @Mapping(source = "eventState", target = "state")
    @Mapping(target = "seasonId", ignore = true)
    public abstract Event toEvent(TgEvent source);


}
