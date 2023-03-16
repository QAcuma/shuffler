package ru.acuma.shuffler.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TgEventPlayer extends TgPlayer {

    private Integer gameCount;
    private Integer spreadScore;
    private Integer sessionScore;
    private Integer lastChange;
    private Boolean left;
    private TgEventPlayer lastGamePlayer;

    @Override
    public Integer applyRating(int change) {
        var applied = super.applyRating(change);
        sessionScore = getCalibrated() ? sessionScore + applied : 0;
        lastChange = applied;

        return applied;
    }

    public void increaseGameCount() {
        gameCount++;
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
            (getCalibrated() ? getSessionRatingToString() : "");
    }

    private String strikethroughBegin() {
        return getLeft() ? "<s>" : "";
    }

    private String strikethroughEnd() {
        return getLeft() ? "</s>" : "";
    }

    String getSessionRatingToString() {
        return getSessionScore() > 0
               ? " (+" + getSessionScore() + ")"
               : " (" + getSessionScore() + ")";
    }
}
