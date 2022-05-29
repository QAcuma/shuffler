package ru.acuma.k.shuffler.mapper;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Component;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.tables.pojos.Event;

@Component
public class EventMapper extends BaseMapper {

    public Event toGroupInfo(KickerEvent kickerEvent) {
        mapperFactory.classMap(KickerEvent.class, Event.class)
                .field("eventState", "status")
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(kickerEvent, Event.class);
    }


}
