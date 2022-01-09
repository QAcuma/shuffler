package ru.acuma.k.shuffler.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
public class KickerEventPlayer extends KickerPlayer {

    private int gameCount;

    private long spreadRating;

    private long sessionRating;

    @Override
    public void plusRating(long value) {
        super.plusRating(value);
        sessionRating += value;
    }

    @Override
    public void minusRating(long value) {
        if (this.getRating() > value) {
            super.minusRating(value);
            sessionRating -= value;
        } else {
            sessionRating -= value - getRating();
            this.setRating(1);
        }
    }

    private boolean left;

    public void gg() {
        gameCount++;
    }

    public String getName() {
        StringBuilder builder = new StringBuilder();
        return builder.append(super.getFirstName())
                .append(" ")
                .append(Optional.ofNullable(super.getLastName()).orElse("Doe"))
                .toString();
    }

    public String getLobbyName() {
        StringBuilder builder = new StringBuilder();
        return builder
                .append(strikethroughBegin())
                .append(super.getFirstName())
                .append(" ")
                .append(Optional.ofNullable(super.getLastName()).orElse("Doe"))
                .append(strikethroughEnd())
                .append(" ")
                .append(super.getRating())
                .append(getSessionRatingToString())
                .toString();
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
        if (getSessionRating() > 0) {
            return " (+" + this.getSessionRating() + ")";
        }
        return " (" + this.getSessionRating() + ")";
    }

}
