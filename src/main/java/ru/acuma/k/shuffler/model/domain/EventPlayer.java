package ru.acuma.k.shuffler.model.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventPlayer extends Player {

    public EventPlayer(Player player) {
        this.gameCount = 0;
    }

    private int gameCount;
}
