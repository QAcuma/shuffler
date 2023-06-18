package ru.acuma.shuffler.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.model.domain.TEventContext;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.domain.TRating;
import ru.acuma.shuffler.model.entity.GroupInfo;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.entity.TeamPlayer;
import ru.acuma.shuffler.model.entity.UserInfo;

import java.util.List;

@Mapper(
    config = MapperConfiguration.class,
    uses = {
        RatingMapper.class,
        UserMapper.class
    }
)
public interface PlayerMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "player.id")
    @Mapping(target = "chatId", source = "player.chat.id")
    @Mapping(target = "ratingContext", source = "rating")
    @Mapping(target = "eventContext", constant = "", qualifiedByName = "defaultEventContext")
    @Mapping(target = "userInfo", source = "player.user")
    @Mapping(target = "lastGamePlayer", ignore = true)
    TEventPlayer toTgEventPlayer(Player player, TRating rating);

    @Named("defaultEventContext")
    @Mapping(target = "left", constant = "false")
    @Mapping(target = "gameCount", constant = "0")
    TEventContext defaultEventContext(String empty);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "chat", source = "chat")
    @Mapping(target = "user", source = "user")
    @Transactional(propagation = Propagation.MANDATORY)
    Player defaultPlayer(UserInfo user, GroupInfo chat);

    default List<TeamPlayer> mapTeamPlayers(List<TEventPlayer> players) {
        return players.stream()
            .map(this::toTeamPlayer)
            .toList();
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "player", source = "player")
    TeamPlayer toTeamPlayer(TEventPlayer player);
}
