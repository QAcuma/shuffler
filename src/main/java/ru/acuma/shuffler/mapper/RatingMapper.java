package ru.acuma.shuffler.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.domain.TRating;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.entity.Rating;

@Mapper(
    config = MapperConfiguration.class,
    imports = Constants.class
)
public abstract class RatingMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "score", source = "score")
    @Mapping(target = "eventScoreChange", constant = "0")
    @Mapping(target = "lastScoreChange", constant = "0")
    @Mapping(target = "multiplier", source = "multiplier")
    public abstract TRating toRating(Rating rating);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "score", expression = "java(Constants.BASE_RATING)")
    @Mapping(target = "eventScoreChange", constant = "0")
    @Mapping(target = "lastScoreChange", constant = "0")
    @Mapping(target = "multiplier", constant = "4")
    public abstract TRating defaultRating(Player player, Discipline discipline);
}
