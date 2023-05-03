package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import ru.acuma.shuffler.model.constant.Constants;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.domain.TRating;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.entity.Rating;

@Mapper(
    config = MapperConfiguration.class
)
public abstract class RatingMapper {

    @Value("${rating.calibration.multiplier}")
    protected Integer calibrationMultiplier;

    @Mapping(target = "calibrated", source = "isCalibrated", defaultValue = "false")
    @Mapping(target = "score", source = "score")
    @Mapping(target = "eventScoreChange", constant = "0")
    @Mapping(target = "lastScoreChange", constant = "0")
    @Mapping(target = "calibrationMultiplier", constant = "", qualifiedByName = "mapCalibrationMultiplier")
    public abstract TRating toRatingContext(Rating rating);

    public TRating defaultRating() {
        return TRating.builder()
            .calibrated(Boolean.FALSE)
            .score(Constants.BASE_RATING)
            .eventScoreChange(0)
            .lastScoreChange(0)
            .calibrationMultiplier(mapCalibrationMultiplier(""))
            .build();
    }

    @Named("mapCalibrationMultiplier")
    protected Integer mapCalibrationMultiplier(String empty) {
        return calibrationMultiplier;
    }
}
