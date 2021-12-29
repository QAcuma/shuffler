package ru.acuma.k.shuffler.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.model.entity.KickerPlayer;
import ru.acuma.k.shuffler.tables.pojos.PlayerInfo;

@Component
public class PlayerMapper {

    private final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public PlayerInfo toPlayerInfo(KickerPlayer kickerPlayer) {
        mapperFactory.classMap(User.class, PlayerInfo.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(kickerPlayer, PlayerInfo.class);
    }

    public KickerPlayer toPlayer(PlayerInfo playerInfo) {
        mapperFactory.classMap(User.class, PlayerInfo.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(playerInfo, KickerPlayer.class);
    }

}
