package ru.acuma.shuffler.mapper;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.k.shuffler.tables.pojos.Event;

@Component
public class EventMapper extends BaseMapper {

    public Event toEvent(GameEvent gameEvent) {
        mapperFactory.classMap(GameEvent.class, Event.class)
                .field("eventState", "state")
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(gameEvent, Event.class);
    }


}
