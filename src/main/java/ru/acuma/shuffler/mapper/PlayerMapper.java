package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.acuma.shuffler.model.domain.TgEventContext;
import ru.acuma.shuffler.model.domain.TgEventPlayer;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.entity.Rating;
import ru.acuma.shuffler.model.entity.UserInfo;

@Mapper(
    config = MapperConfiguration.class,
    uses = {
        RatingMapper.class,
        UserMapper.class
    }
)
public abstract class PlayerMapper {

    @Mapping(target = "id", source = "player.id")
    @Mapping(target = "chatId", source = "player.chat.id")
    @Mapping(target = "ratingContext", source = "rating")
    @Mapping(target = "eventContext", constant = "", qualifiedByName = "defaultEventContext")
    @Mapping(target = "userInfo", source = "userInfo")
    @Mapping(target = "lastGamePlayer", ignore = true)
    public abstract TgEventPlayer toTgEventPlayer(Player player, UserInfo userInfo, Rating rating);

    @Named("defaultEventContext")
    protected TgEventContext defaultEventContext(String empty) {
        return TgEventContext.builder()
            .left(Boolean.FALSE)
            .gameCount(0)
            .build();
    }

}
