package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.entity.TgEventPlayer;
import ru.acuma.shuffler.model.enums.Command;
import ru.acuma.shuffler.service.api.KeyboardService;
import ru.acuma.shuffler.service.api.KickService;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KickServiceImpl implements KickService {

    private final KeyboardService keyboardService;

    @Override
    public InlineKeyboardMarkup preparePlayersKeyboard(List<TgEventPlayer> players) {
        var playersMap = players.stream()
                .collect(Collectors.toMap(TgEventPlayer::getTelegramId, TgEventPlayer::getName));
        var cancelButton = keyboardService.getSingleButton(Command.CANCEL_EVICT, "Не исключать");

        return keyboardService.getDynamicListKeyboard(Command.EVICT, playersMap, cancelButton);
    }

    @Override
    public SendMessage prepareKickMessage(AbsSender absSender, TgEvent event) {
        List<TgEventPlayer> players = event.getLatestGame().getPlayers()
                .stream()
                .filter(Predicate.not(TgEventPlayer::isLeft))
                .collect(Collectors.toList());
        var keyboard = preparePlayersKeyboard(players);

        return SendMessage.builder()
                .chatId(String.valueOf(event.getChatId()))
                .replyMarkup(keyboard)
                .text("Выберите игрока для исключения")
                .build();
    }

}
