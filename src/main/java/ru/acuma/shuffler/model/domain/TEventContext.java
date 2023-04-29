package ru.acuma.shuffler.model.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@Accessors(chain = true)
public class TEventContext implements Serializable {
    private Integer gameCount;
    private Boolean left;

    public void increaseGameCount() {
        gameCount++;
    }
}
