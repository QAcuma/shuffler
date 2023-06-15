package ru.acuma.shuffler.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class TEventContext implements Serializable {
    private Integer gameCount;
    private Boolean left;

    public void increaseGameCount() {
        gameCount++;
    }
}
