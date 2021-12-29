package ru.acuma.k.shuffler.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KickerEventPlayer extends KickerPlayer {

    public KickerEventPlayer(KickerPlayer kickerPlayer) {
        this.gameCount = 0;
    }

    private int gameCount;
}
