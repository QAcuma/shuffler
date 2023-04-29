package ru.acuma.shuffler.model.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Optional;

@Data
@Builder
@Accessors(chain = true)
public class TEventPlayer implements Serializable {
    private Long id;
    private Long chatId;
    private TRating ratingContext;
    private TEventContext eventContext;
    private TEventPlayer lastGamePlayer;
    private TUserInfo userInfo;

    public void applyRating(Integer change) {
        ratingContext.applyScore(change);
    }

    public Integer getScoreSorting() {
        return ratingContext.getCalibrated()
               ? ratingContext.getScore()
               : 0;
    }

    public String getScoreString() {
        return ratingContext.getCalibrated()
               ? String.valueOf(ratingContext.getScore())
               : "calibrating";
    }

    public Boolean isCalibrated() {
        return ratingContext.getCalibrated();
    }

    public Boolean isLeft() {
        return eventContext.getLeft();
    }

    public String getNameWithRating() {
        return userInfo.getFirstName() +
            " " +
            Optional.ofNullable(userInfo.getLastName()).orElse("Doe") +
            this.getScoreString();
    }

    public void increaseGameCount() {
        eventContext.increaseGameCount();
    }

    public String getName() {
        return getUserInfo().getFirstName() +
            " " +
            getFormatName();
    }

    private String getFormatName() {
        String lastName = "";
        if (getUserInfo().getLastName() != null) {
            lastName = getUserInfo().getLastName().charAt(0) + ".";
        }
        StringUtils.capitalize(lastName);

        return lastName;
    }

    public String getLobbyName() {
        return strikethroughBegin() +
            getName() +
            strikethroughEnd() +
            " " +
            getScoreString() +
            (ratingContext.getCalibrated() ? getSessionRatingToString() : "");
    }

    private String strikethroughBegin() {
        return eventContext.getLeft() ? "<s>" : "";
    }

    private String strikethroughEnd() {
        return eventContext.getLeft() ? "</s>" : "";
    }

    String getSessionRatingToString() {
        return ratingContext.getEventScoreChange() > 0
               ? " (+" + ratingContext.getEventScoreChange() + ")"
               : " (" + ratingContext.getEventScoreChange() + ")";
    }
}
