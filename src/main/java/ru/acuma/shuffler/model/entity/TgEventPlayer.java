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

    @Override
    public void plusRating(long value) {
        super.plusRating(value);
        sessionScore += value;
    }

    @Override
    public void minusRating(long value) {
        if (this.getScore() > value) {
            super.minusRating(value);
            sessionScore -= value;
        } else {
            sessionScore -= value - this.getScore();
            this.setScore(1);
        }
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
                super.getScore() +
                getSessionRatingToString();
    }

    private String strikethroughBegin() {
        if (left) {
            return "<s>";
        }
        return "";
    }

    private String strikethroughEnd() {
        if (left) {
            return "</s>";
        }
        return "";
    }

    String getSessionRatingToString() {
        if (this.getSessionScore() > 0) {
            return " (+" + this.getSessionScore() + ")";
        }
        return " (" + this.getSessionScore() + ")";
    }

}
