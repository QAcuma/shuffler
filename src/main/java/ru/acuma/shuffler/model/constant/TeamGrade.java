package ru.acuma.shuffler.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TeamGrade {

    STRONG(-1),
    WEAK(1);

    private final int factor;

}
