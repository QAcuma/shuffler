package ru.acuma.shuffler.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.acuma.shuffler.model.domain.TEvent;
import ru.acuma.shuffler.model.domain.TEventPlayer;
import ru.acuma.shuffler.model.constant.Command;
import ru.acuma.shuffler.service.api.KickService;
import ru.acuma.shuffler.service.message.KeyboardService;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KickServiceImpl implements KickService {

    private final KeyboardService keyboardService;

    @Override
    public InlineKeyboardMarkup preparePlayersKeyboard(List<TEventPlayer> players) {
        var playersMap = players.stream()
                .collect(Collectors.toMap(
                        eventPlayer -> eventPlayer.getUserInfo().getTelegramId(),
                        TEventPlayer::getName)
                );
        var cancelButton = keyboardService.getSingleButton(Command.CANCEL_EVICT, "Не исключать");

        return keyboardService.getDynamicListKeyboard(Command.EVICT, playersMap, cancelButton);
    }

    @Override
    public SendMessage prepareKickMessage(TEvent event) {
        List<TEventPlayer> players = event.getCurrentGame().getPlayers()
                .stream()
                .filter(Predicate.not(TEventPlayer::isLeft))
                .toList();
        var keyboard = preparePlayersKeyboard(players);

        return SendMessage.builder()
                .chatId(String.valueOf(event.getChatId()))
                .replyMarkup(keyboard)
                .text("Выберите игрока для исключения")
                .build();
    }

}
