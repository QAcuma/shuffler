package ru.acuma.shuffler.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.domain.TRating;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.entity.Rating;

import java.util.Objects;

@Mapper(
    config = MapperConfiguration.class,
    imports = Constants.class
)
public abstract class RatingMapper {

    @Value("${rating.calibration.multiplier}")
    protected Integer calibrationMultiplier;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "calibrated", source = "isCalibrated", defaultValue = "false")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "score", source = "score")
    @Mapping(target = "eventScoreChange", constant = "0")
    @Mapping(target = "lastScoreChange", constant = "0")
    @Mapping(target = "calibrationMultiplier", constant = "1000", qualifiedByName = "mapCalibrationMultiplier")
    public abstract TRating toRating(Rating rating);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "calibrated", constant = "false")
    @Mapping(target = "score", expression = "java(Constants.BASE_RATING)")
    @Mapping(target = "eventScoreChange", constant = "0")
    @Mapping(target = "lastScoreChange", constant = "0")
    @Mapping(target = "calibrationMultiplier", constant = "1000", qualifiedByName = "mapCalibrationMultiplier")
    public abstract TRating defaultRating(Player player, Discipline discipline);

    @Named("mapCalibrationMultiplier")
    protected Integer mapCalibrationMultiplier(Integer mock) {
        return Objects.nonNull(calibrationMultiplier)
               ? calibrationMultiplier
               : mock;
    }
}
