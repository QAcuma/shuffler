package ru.acuma.shuffler.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCause {

    USER_NOT_FOUND("User with id %s not found in database"),
    PLAYER_NOT_FOUND("Player with userId %s, chatId %s not found in database"),
    GROUP_NOT_FOUND("Group with id %s not found in database"),
    EVENT_NOT_FOUND("Event with id %s not found in database"),
    GAME_NOT_FOUND("Game with id %s not found in database"),
    GAME_NOT_EXISTS("Game nor found for chat %s"),
    CHAT_IS_PRIVATE("Chat is private"),
    CHAT_IS_CHANNEL("Chat is channel"),
    USER_IS_NOT_ACTIVE("User with id %s not active"),
    GROUP_IS_NOT_ACTIVE("Group with id %s not active"),
    CALLBACK_MESSAGE_MISSING("Callback message is null"),
    MESSAGE_NOT_FOUND("Message with type %s not fount in event context"),
    MESSAGE_FROM_MISSING("Callback message from is null"),
    RATING_NOT_FOUND("Rating with id %s not found in database"),
    DISCIPLINE_NOT_PRESENT("Discipline %s not present"),
    CANNOT_EXTRACT_PARAM("Can't extract param %s"),
    EXTRACT_RESPONSE_MESSAGE("Can't extract response message"),
    EVENT_CONTEXT_IS_EMPTY("Event context game count is not present"),
    STORABLE_TASK_NOT_IMPLEMENTED("No implementation for task with type %s"),
    MISSING_WINNER_TEAM("Can't find winner's team");

    private final String description;

}
