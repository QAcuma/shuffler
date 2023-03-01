package ru.acuma.shuffler.model.dto;

import lombok.Getter;

import java.io.Serializable;

@Getter
public final class TgGameBet implements Serializable {

    private final int caseWin;
    private final int caseLose;

    public TgGameBet(int caseWin, int caseLose) {
        this.caseWin = caseWin;
        this.caseLose = caseLose;
    }
}
