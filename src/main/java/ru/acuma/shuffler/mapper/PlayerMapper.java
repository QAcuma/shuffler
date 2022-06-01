package ru.acuma.shuffler.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.model.entity.GameEventPlayer;
import ru.acuma.shuffler.model.entity.GamePlayer;
import ru.acuma.k.shuffler.tables.pojos.Rating;
import ru.acuma.k.shuffler.tables.pojos.UserInfo;

@Component
public class PlayerMapper extends BaseMapper {

    private final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public GamePlayer toKickerPlayer(UserInfo userInfo, ru.acuma.k.shuffler.tables.pojos.Player player, Rating rating) {
        mapperFactory.classMap(UserInfo.class, GamePlayer.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        GamePlayer kickerGamePlayer = mapper.map(userInfo, GamePlayer.class);
        return kickerGamePlayer
                .setId(player.getId())
                .setChatId(player.getChatId())
                .setRating(rating.getRating());
    }

    public GameEventPlayer toKickerEventPlayer(GamePlayer gamePlayer) {
        mapperFactory.classMap(GamePlayer.class, GameEventPlayer.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        GameEventPlayer eventPlayer = mapper.map(gamePlayer, GameEventPlayer.class);
        return eventPlayer.setGameCount(0)
                .setSessionRating(0)
                .setLeft(false);
    }


}
