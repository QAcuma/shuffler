package ru.acuma.shuffler.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@Accessors(chain = true)
public class TgEventPlayer extends TgPlayer {

    private int gameCount;
    private long spreadScore;
    private int sessionScore;
    private TgEventPlayer lastGamePlayer;
    private boolean left;
    private int lastChange;

    @Override
    public int applyRating(int change) {
        var applied = super.applyRating(change);
        sessionScore = isCalibrated() ? sessionScore + applied : 0;
        lastChange = applied;

        return applied;
    }

    public void increaseGameCount() {
        gameCount++;
    }

    public String getName() {
        return super.getFirstName() +
                " " +
                getFormatName();
    }

    private String getFormatName() {
        String lastName = "";
        if (getLastName() != null) {
            lastName = getLastName().charAt(0) + ".";
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
                (isCalibrated() ? getSessionRatingToString() : "");
    }

    private String strikethroughBegin() {
        return isLeft() ? "<s>" : "";
    }

    private String strikethroughEnd() {
        return isLeft() ? "</s>" : "";
    }

    String getSessionRatingToString() {
        return getSessionScore() > 0
                ? " (+" + getSessionScore() + ")"
                : " (" + getSessionScore() + ")";
    }
}
