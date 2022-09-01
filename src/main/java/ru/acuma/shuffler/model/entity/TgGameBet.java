package ru.acuma.shuffler.model.entity;

import lombok.Getter;

@Getter
public final class TgGameBet {

    private final int caseWin;
    private final int caseLose;

    public TgGameBet(int caseWin, int caseLose) {
        this.caseWin = caseWin;
        this.caseLose = caseLose;
    }
}
