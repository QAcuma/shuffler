package ru.acuma.k.shuffler.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;
import ru.acuma.k.shuffler.model.entity.KickerPlayer;
import ru.acuma.k.shuffler.tables.pojos.Player;
import ru.acuma.k.shuffler.tables.pojos.Rating;
import ru.acuma.k.shuffler.tables.pojos.UserInfo;

@Component
public class PlayerMapper extends BaseMapper {

    private final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public KickerPlayer toKickerPlayer(UserInfo userInfo, Player player, Rating rating) {
        mapperFactory.classMap(UserInfo.class, KickerPlayer.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        KickerPlayer kickerPlayer = mapper.map(userInfo, KickerPlayer.class);
        return kickerPlayer
                .setId(player.getId())
                .setChatId(player.getChatId())
                .setRating(rating.getRating());
    }

    public KickerEventPlayer toKickerEventPlayer(KickerPlayer player) {
        mapperFactory.classMap(KickerPlayer.class, KickerEventPlayer.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        KickerEventPlayer eventPlayer = mapper.map(player, KickerEventPlayer.class);
        return eventPlayer.setGameCount(0)
                .setSessionRating(0)
                .setLeft(false);
    }


}
