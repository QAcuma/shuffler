package ru.acuma.shuffler.model.enums.keyboards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.buttons.EventStatusButton;

import static ru.acuma.shuffler.util.Symbols.PRIDE_EMOJI;

@Getter
@AllArgsConstructor
public enum Ready implements EventStatusButton {

    BUTTON_JOIN(Command.JOIN.getCommand(), "⚽️ Играю!", 1),
    BUTTON_LEAVE(Command.LEAVE.getCommand(), PRIDE_EMOJI + "Не могу", 1),
    BUTTON_CANCEL(Command.CANCEL.getCommand(), "❌ Отменить турнир ❌", 2),
    BUTTON_SHUFFLE(Command.BEGIN.getCommand(), "✅ Начать турнир ✅", 3);

    public final String action;
    public final String alias;
    public final int row;

}
