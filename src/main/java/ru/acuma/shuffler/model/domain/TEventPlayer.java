package ru.acuma.shuffler.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Optional;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class TEventPlayer implements Serializable {
    private Long id;
    private Long chatId;
    @EqualsAndHashCode.Exclude
    private TEventPlayer lastGamePlayer;
    private TRating ratingContext;
    private TEventContext eventContext;
    private TUserInfo userInfo;

    public void applyRating(Integer change) {
        ratingContext.applyScore(change);
    }

    public Long getUserId() {
        return userInfo.getUserId();
    }

    public Integer getScoreSorting() {
        return Boolean.TRUE.equals(ratingContext.getCalibrated())
               ? ratingContext.getScore()
               : 0;
    }

    public String getScoreString() {
        return Boolean.TRUE.equals(ratingContext.getCalibrated())
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
            (Boolean.TRUE.equals(ratingContext.getCalibrated()) ? getSessionRatingToString() : "");
    }

    private String strikethroughBegin() {
        return Boolean.TRUE.equals(eventContext.getLeft()) ? "<s>" : "";
    }

    private String strikethroughEnd() {
        return Boolean.TRUE.equals(eventContext.getLeft()) ? "</s>" : "";
    }

    String getSessionRatingToString() {
        return ratingContext.getEventScoreChange() > 0
               ? " (+" + ratingContext.getEventScoreChange() + ")"
               : " (" + ratingContext.getEventScoreChange() + ")";
    }
}
