package ru.acuma.shuffler.model.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@Accessors(chain = true)
public final class TGameBet implements Serializable {

    private final Integer caseWin;
    private final Integer caseLose;

}
