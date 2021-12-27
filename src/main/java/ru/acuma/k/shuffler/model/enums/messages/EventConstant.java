package ru.acuma.k.shuffler.model.enums.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.k.shuffler.service.enums.EventConstantApi;

@Getter
@AllArgsConstructor
public enum EventConstant implements EventConstantApi {

    LOBBY_MESSAGE("Самое время крутить шашлыки!\n" +
            "\n" +
            "Участники:\n"),
    CHECKING_LEAVE_MESSAGE(", ты точно хочешь покинуть игру?");

    private final String text;

}
