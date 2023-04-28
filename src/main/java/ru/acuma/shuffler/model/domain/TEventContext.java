package ru.acuma.shuffler.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class TEventContext implements Serializable {
    private Integer gameCount;
    private Boolean left;

    public void increaseGameCount() {
        gameCount++;
    }
}
