package ru.acuma.shuffler.service.season;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.acuma.shuffler.model.entity.GroupInfo;
import ru.acuma.shuffler.repository.GroupInfoRepository;
import ru.acuma.shuffler.repository.PlayerRepository;
import ru.acuma.shuffler.service.api.ExecuteService;
import ru.acuma.shufflerlib.model.Discipline;
import ru.acuma.shufflerlib.model.web.entity.WebPlayer;
import ru.acuma.shufflerlib.model.web.entity.WebPlayerDetails;

import java.util.Arrays;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class BroadcastService {

    private final GroupInfoRepository groupInfoRepository;
    private final PlayerRepository playerRepository;
    private final ExecuteService executeService;
    private final TextBuilderService textBuilderService;

    @Value("${rating.calibration.games}")
    private int requiredGames;

    public void seasonResultBroadcast(Long seasonId) {
        groupInfoRepository.findAllBySeasonIdAndIsBlockedIsFalse(seasonId)
            .forEach(groupInfo -> seasonResultBroadcast(seasonId, groupInfo));
    }

    public void seasonResultBroadcast(Long seasonId, GroupInfo groupInfo) {
        Arrays.stream(Discipline.values())
            .forEach(discipline -> seasonResultBroadcast(seasonId, groupInfo, discipline));
    }

    public void seasonResultBroadcast(Long seasonId, GroupInfo groupInfo, Discipline discipline) {
        var ladderResult = playerRepository.findAllActiveBySeasonIdAndChatIdAndDiscipline(
            seasonId,
            groupInfo.getId(),
            discipline
        );
//        ladderResult.stream()
//            .map(user)
//            .filter(this::filterNonCalibratedPlayers)
//            .sorted(Comparator.comparingLong(WebPlayer::getScore).reversed())
//            .toList();

        if (ladderResult.isEmpty()) {
            return;
        }

        var message = SendMessage.builder()
            .chatId(String.valueOf(groupInfo.getId()))
            .parseMode(ParseMode.MARKDOWNV2)
            .text(textBuilderService.buildSeasonReport(null))
            .build();

        executeService.execute(message);
    }

    private boolean filterNonCalibratedPlayers(WebPlayerDetails player) {
        return player.getWinCount() + player.getLoseCount() >= requiredGames;
    }

}
