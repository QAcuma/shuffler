package ru.acuma.shuffler.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.acuma.shuffler.model.constant.messages.MenuScreen;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class TMenu {

    private Long chatId;
    private MenuScreen currentScreen;

}
