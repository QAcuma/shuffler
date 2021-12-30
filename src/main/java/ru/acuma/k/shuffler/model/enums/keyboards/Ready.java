package ru.acuma.k.shuffler.model.enums.keyboards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.acuma.k.shuffler.model.enums.Command;
import ru.acuma.k.shuffler.service.enums.EventStatusApi;

@Getter
@AllArgsConstructor
public enum Ready implements EventStatusApi {

    BUTTON_JOIN(Command.JOIN.getCommand(), "⚽️ Играю!", 1),
    BUTTON_LEAVE(Command.LEAVE.getCommand(), "\uD83E\uDDD1\uD83C\uDFFF\u200D\uD83E\uDDBC Не могу", 1),
    BUTTON_CANCEL(Command.CANCEL.getCommand(), "❌ Отменить турнир ❌", 2),
    BUTTON_SHUFFLE(Command.BEGIN.getCommand(), "✅ Начать турнир ✅", 3);

    public final String action;
    public final String alias;
    public final int row;

}
