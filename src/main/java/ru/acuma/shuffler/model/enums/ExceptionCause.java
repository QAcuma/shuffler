package ru.acuma.shuffler.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCause {

    USER_NOT_FOUND("User with id %s not found in database"),
    GROUP_NOT_FOUND("Group with id %s not found in database"),
    RATING_NOT_FOUND("Rating with id %s not found in database");

    private final String description;

}
