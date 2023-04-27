package ru.acuma.shuffler.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCause {

    USER_NOT_FOUND("User with id %s not found in database"),
    USER_IS_NOT_ACTIVE("User with id %s not active"),
    GROUP_NOT_FOUND("Group with id %s not found in database"),
    GROUP_IS_NOT_ACTIVE("Group with id %s not active"),
    CHAT_IS_PRIVATE("Chat is private"),
    MESSAGE_MISSING("Callback message is null"),
    MESSAGE_FROM_MISSING("Callback message from is null"),
    RATING_NOT_FOUND("Rating with id %s not found in database");

    private final String description;

}
