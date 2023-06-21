package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.domain.TRating;
import ru.acuma.shuffler.model.entity.Game;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.entity.RatingHistory;
import ru.acuma.shuffler.model.entity.Season;

@Mapper(
    config = MapperConfiguration.class,
    imports = PlayerMapper.class
)
public interface RatingHistoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "change", source = "rating.lastScoreChange")
    @Mapping(target = "score", source = "rating.score")
    @Mapping(target = "player", source = "player")
    @Mapping(target = "game", source = "game")
    @Mapping(target = "season", source = "season")
    @Mapping(target = "discipline", source = "discipline")
    RatingHistory toHistoryRecord(TRating rating, Player player, Game game, Season season, Discipline discipline);
}
