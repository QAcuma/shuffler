package ru.acuma.shuffler.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TeamStatus {

    STRONG(1),
    WEAK(-1);

    private final int factor;


}
