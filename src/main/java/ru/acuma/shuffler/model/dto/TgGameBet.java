package ru.acuma.shuffler.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@Accessors(chain = true)
public final class TgGameBet implements Serializable {

    private final Integer caseWin;
    private final Integer caseLose;

    public TgGameBet(int caseWin, int caseLose) {
        this.caseWin = caseWin;
        this.caseLose = caseLose;
    }
}
