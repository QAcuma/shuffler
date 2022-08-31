package ru.acuma.shuffler.model.entity;

import lombok.Getter;
import ru.acuma.shuffler.model.enums.Values;

@Getter
public final class TgGameBet {

    private final int caseWin;
    private final int caseLose;

    public TgGameBet(int caseWin) {
        this.caseWin = caseWin;
        this.caseLose = (Values.BASE_RATING_CHANGE * 2 - caseWin) * -1;
    }
}
