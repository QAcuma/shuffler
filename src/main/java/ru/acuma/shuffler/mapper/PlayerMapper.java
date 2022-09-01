package ru.acuma.shuffler.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${rating.calibration.multiplier}")
    private int calibrationMultiplier;

    public TgPlayer toTgPlayer(UserInfo userInfo, Player player, Rating rating) {
        mapperFactory.classMap(UserInfo.class, TgPlayer.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        TgPlayer tgPlayer = mapper.map(userInfo, TgPlayer.class);

        return tgPlayer
                .setId(player.getId())
                .setCalibrated(rating.getIsCalibrated())
                .setChatId(player.getChatId())
                .setScore(rating.getScore())
                .setCalibrationMultiplier(calibrationMultiplier);
    }

    public TgEventPlayer toTgEventPlayer(TgPlayer tgPlayer) {
        mapperFactory.classMap(TgPlayer.class, TgEventPlayer.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        TgEventPlayer eventPlayer = mapper.map(tgPlayer, TgEventPlayer.class);

        return eventPlayer.setGameCount(0)
                .setSessionScore(0)
                .setLeft(false);
    }

    public TgEventPlayer toTgEventPlayer(UserInfo userInfo, Player player, Rating rating) {
        mapperFactory.classMap(TgPlayer.class, TgEventPlayer.class)
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        var tgPlayer = toTgPlayer(userInfo, player, rating);
        TgEventPlayer eventPlayer = toTgEventPlayer(tgPlayer);
        eventPlayer.setCalibrationMultiplier(calibrationMultiplier);

        return eventPlayer.setGameCount(0)
                .setSessionScore(0)
                .setLeft(false);
    }


}
