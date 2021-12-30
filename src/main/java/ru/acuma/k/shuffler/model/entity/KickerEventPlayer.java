package ru.acuma.k.shuffler.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class KickerEventPlayer extends KickerPlayer {

    private int gameCount;

    private boolean left;

}
