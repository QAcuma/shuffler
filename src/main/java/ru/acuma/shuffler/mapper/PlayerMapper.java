package ru.acuma.shuffler.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;
import ru.acuma.shuffler.model.entity.TgEventPlayer;
import ru.acuma.shuffler.model.entity.TgPlayer;
import ru.acuma.shuffler.tables.pojos.Player;
import ru.acuma.shuffler.tables.pojos.Rating;
import ru.acuma.shuffler.tables.pojos.UserInfo;

@Mapper(componentModel = "spring")
public abstract class PlayerMapper {

    @Value("${rating.calibration.multiplier}")
    protected int calibrationMultiplier;

    @Mapping(source = "player.id", target = "id")
    @Mapping(source = "player.chatId", target = "chatId")
    @Mapping(source = "rating.score", target = "score")
    @Mapping(source = "rating.isCalibrated", target = "calibrated")
    @Mapping(target = "calibrationMultiplier", expression = "java(calibrationMultiplier)")
    public abstract TgPlayer toTgPlayer(UserInfo source, Player player, Rating rating);

    @Mapping(target = "calibrationMultiplier", expression = "java(calibrationMultiplier)")
    @Mapping(target = "gameCount", constant = "0")
    @Mapping(target = "sessionScore", constant = "0")
    @Mapping(target = "left", constant = "false")
    public abstract TgEventPlayer toTgEventPlayer(TgPlayer source);

    public TgEventPlayer toTgEventPlayer(UserInfo source, @Context Player player, @Context Rating rating) {
        var tgPlayer = toTgPlayer(source, player, rating);
        var eventPlayer = toTgEventPlayer(tgPlayer);
        eventPlayer.setCalibrationMultiplier(calibrationMultiplier);

        return eventPlayer;
    }


}
