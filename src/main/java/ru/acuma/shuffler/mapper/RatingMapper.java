package ru.acuma.shuffler.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.domain.TRating;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.entity.Rating;
import ru.acuma.shuffler.model.entity.Season;

@Mapper(
    config = MapperConfiguration.class,
    imports = Constants.class
)
public interface RatingMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "score", source = "score")
    @Mapping(target = "eventScoreChange", constant = "0")
    @Mapping(target = "lastScoreChange", constant = "0")
    @Mapping(target = "multiplier", source = "multiplier")
    TRating toRating(Rating rating);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "rating.id")
    @Mapping(target = "score", source = "rating.score")
    @Mapping(target = "multiplier", source = "rating.multiplier")
    @Mapping(target = "season", source = "season")
    @Mapping(target = "player", source = "player")
    @Mapping(target = "discipline", source = "discipline")
    Rating toRating(TRating rating, Season season, Player player, Discipline discipline);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "score", expression = "java(Constants.BASE_RATING)")
    @Mapping(target = "eventScoreChange", constant = "0")
    @Mapping(target = "lastScoreChange", constant = "0")
    @Mapping(target = "multiplier", constant = "3")
    TRating defaultRating(Player player, Discipline discipline);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "score", source = "score")
    @Mapping(target = "multiplier", source = "multiplier")
    void update(@MappingTarget Rating rating, TRating ratingContext);
}
