package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.domain.TGame;
import ru.acuma.shuffler.model.entity.RatingHistory;
import ru.acuma.shuffler.model.entity.Season;

@Mapper(
    config = MapperConfiguration.class,
    imports = PlayerMapper.class
)
public interface RatingHistoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "player", source = "player")
    @Mapping(target = "game", source = "game")
    @Mapping(target = "change", source = "player.ratingContext.lastScoreChange")
    @Mapping(target = "score", source = "player.ratingContext.score")
    @Mapping(target = "season", source = "season")
    @Mapping(target = "discipline", source = "discipline")
    RatingHistory toHistoryRecord(TEventPlayer player, TGame game, Season season, Discipline discipline);
}
