package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import ru.acuma.shuffler.model.dto.TgEventPlayer;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.entity.Rating;
import ru.acuma.shuffler.model.entity.UserInfo;

@Mapper(
    config = MapperConfiguration.class,
    uses = UserMapper.class
)
public abstract class PlayerMapper {

    @Value("${rating.calibration.multiplier}")
    protected Integer calibrationMultiplier;

    @Mapping(target = "gameCount", constant = "0")
    @Mapping(target = "spreadScore", constant = "0")
    @Mapping(target = "sessionScore", constant = "0")
    @Mapping(target = "left", constant = "false")
    @Mapping(target = "id", source = "player.id")
    @Mapping(target = "chatId", source = "player.chat.id")
    @Mapping(target = "score", source = "rating.score")
    @Mapping(target = "calibrated", source = "rating.isCalibrated")
    @Mapping(target = "calibrationMultiplier", source = ".", qualifiedByName = "mapCalibrationMultiplier")
    @Mapping(target = "userInfo", source = "userInfo")
    public abstract TgEventPlayer toTgEventPlayer(UserInfo userInfo, Player player, Rating rating);

    @Named("mapCalibrationMultiplier")
    protected Integer mapCalibrationMultiplier() {
        return calibrationMultiplier;
    }

}
