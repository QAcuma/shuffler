package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TEventPlayer;

import java.util.List;

public interface KickService {

    InlineKeyboardMarkup preparePlayersKeyboard(List<TEventPlayer> players);

    SendMessage prepareKickMessage(TEvent event);
}
