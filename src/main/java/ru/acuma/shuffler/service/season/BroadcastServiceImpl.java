package ru.acuma.shuffler.service.season;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.acuma.shuffler.service.api.BroadcastService;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shuffler.tables.pojos.GroupInfo;
import ru.acuma.shufflerlib.model.Discipline;
import ru.acuma.shufflerlib.model.Filter;
import ru.acuma.shufflerlib.model.web.entity.WebPlayer;
import ru.acuma.shufflerlib.model.web.entity.WebPlayerDetails;
import ru.acuma.shufflerlib.repository.GroupRepository;
import ru.acuma.shufflerlib.repository.PlayerRepository;

import java.util.Arrays;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class BroadcastServiceImpl implements BroadcastService {

    private final GroupRepository groupRepository;
    private final PlayerRepository playerRepository;
    private final ExecuteService executeService;
    private final TextBuilderService textBuilderService;

    @Value("${rating.calibration.games}")
    private int requiredGames;

    @Override
    public void seasonResultBroadcast(Long seasonId) {
        groupRepository.findActiveGroups(seasonId)
                .forEach(groupInfo -> seasonResultBroadcast(seasonId, groupInfo));
    }

    @Override
    public void seasonResultBroadcast(Long seasonId, GroupInfo groupInfo) {
        Arrays.stream(Discipline.values())
                .forEach(discipline -> seasonResultBroadcast(seasonId, groupInfo, discipline));
    }

    @Override
    public void seasonResultBroadcast(Long seasonId, GroupInfo groupInfo, Discipline discipline) {
        var filter = Filter.builder()
                .seasonId(seasonId)
                .discipline(discipline)
                .chatId(groupInfo.getChatId())
                .chatName(groupInfo.getName())
                .build();

        var ladderResult = playerRepository.findActivePlayers(filter)
                .stream()
                .map(playerId -> playerRepository.findPlayerInfo(filter.setPlayerId(playerId)))
                .filter(this::filterLazy)
                .sorted(Comparator.comparingLong(WebPlayer::getScore).reversed())
                .toList();

        if (ladderResult.isEmpty()) {
            return;
        }

        var message = SendMessage.builder()
                .chatId(String.valueOf(groupInfo.getChatId()))
                .parseMode(ParseMode.MARKDOWNV2)
                .text(textBuilderService.buildSeasonReport(ladderResult))
                .build();

        executeService.execute(message);
    }

    private boolean filterLazy(WebPlayerDetails player) {
        return player.getWinCount() + player.getLoseCount() >= requiredGames;
    }

}
