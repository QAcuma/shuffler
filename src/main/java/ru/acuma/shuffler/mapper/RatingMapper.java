package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import ru.acuma.shuffler.model.domain.TgRatingContext;
import ru.acuma.shuffler.model.entity.Rating;

@Mapper(
    config = MapperConfiguration.class
)
public abstract class RatingMapper {

    @Value("${rating.calibration.multiplier}")
    protected Integer calibrationMultiplier;

    @Mapping(target = "calibrated", source = "isCalibrated", defaultValue = "false")
    @Mapping(target = "eventScoreChange", constant = "0")
    @Mapping(target = "calibrationMultiplier", constant = "", qualifiedByName = "mapCalibrationMultiplier")
    public abstract TgRatingContext toRatingContext(Rating rating);

    @Named("mapCalibrationMultiplier")
    protected Integer mapCalibrationMultiplier(String empty) {
        return calibrationMultiplier;
    }

}
