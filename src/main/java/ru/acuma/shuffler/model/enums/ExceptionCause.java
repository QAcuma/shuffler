package ru.acuma.shuffler.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCause {

    USER_NOT_FOUND("User with id %s not found in database"),
    USER_IS_NOT_ACTIVE("User with id %s not active"),
    CHAT_IS_NOT_ACTIVE("Chat with id %s not active"),
    GROUP_NOT_FOUND("Group with id %s not found in database"),
    RATING_NOT_FOUND("Rating with id %s not found in database");

    private final String description;

}
