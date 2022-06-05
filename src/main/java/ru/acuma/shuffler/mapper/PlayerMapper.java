package ru.acuma.shuffler.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.model.entity.TgEventPlayer;
import ru.acuma.shuffler.model.entity.TgPlayer;
import ru.acuma.shuffler.tables.pojos.Player;
import ru.acuma.shuffler.tables.pojos.Rating;
import ru.acuma.shuffler.tables.pojos.UserInfo;
import ru.acuma.shufflerlib.mapper.BaseMapper;

@Component
public class PlayerMapper extends BaseMapper {

    private final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public TgPlayer toTgPlayer(UserInfo userInfo, Player player, Rating rating) {
        mapperFactory.classMap(UserInfo.class, TgPlayer.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        TgPlayer tgPlayer = mapper.map(userInfo, TgPlayer.class);
        return tgPlayer
                .setId(player.getId())
                .setChatId(player.getChatId())
                .setRating(rating.getRating());
    }

    public TgEventPlayer toTgEventPlayer(TgPlayer tgPlayer) {
        mapperFactory.classMap(TgPlayer.class, TgEventPlayer.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        TgEventPlayer eventPlayer = mapper.map(tgPlayer, TgEventPlayer.class);
        return eventPlayer.setGameCount(0)
                .setSessionRating(0)
                .setLeft(false);
    }


}
