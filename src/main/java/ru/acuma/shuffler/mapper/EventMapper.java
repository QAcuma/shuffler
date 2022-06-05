package ru.acuma.shuffler.mapper;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.tables.pojos.Event;

@Component
public class EventMapper extends BaseMapper {

    public Event toEvent(TgEvent tgEvent) {
        mapperFactory.classMap(TgEvent.class, Event.class)
                .field("eventState", "state")
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(tgEvent, Event.class);
    }


}
