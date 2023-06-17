package ru.acuma.shuffler.service.season;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.entity.GroupInfo;
import ru.acuma.shuffler.repository.GroupInfoRepository;
import ru.acuma.shuffler.repository.PlayerRepository;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class BroadcastService {

    private final GroupInfoRepository groupInfoRepository;
    private final PlayerRepository playerRepository;
    private final TextBuilderService textBuilderService;

    public void seasonResultBroadcast(Long seasonId) {
        groupInfoRepository.findAllByIsActiveTrue()
            .forEach(groupInfo -> seasonResultBroadcast(seasonId, groupInfo));
    }

    public void seasonResultBroadcast(Long seasonId, GroupInfo groupInfo) {
        Arrays.stream(Discipline.values())
            .forEach(discipline -> seasonResultBroadcast(seasonId, groupInfo, discipline));
    }

    public void seasonResultBroadcast(Long seasonId, GroupInfo groupInfo, Discipline discipline) {
//        var ladderResult = playerRepository.findAllActiveBySeasonIdAndChatIdAndDiscipline(
//            seasonId,
//            groupInfo.getId(),
//            discipline
//        );
//        ladderResult.stream()
//            .map(user)
//            .sorted(Comparator.comparingLong(WebPlayer::getScore).reversed())
//            .toList();

//        if (ladderResult.isEmpty()) {
        return;
    }

//        var message = SendMessage.builder()
//            .chatId(String.valueOf(groupInfo.getId()))
//            .parseMode(ParseMode.MARKDOWNV2)
//            .text(textBuilderService.buildSeasonReport(null))
//            .build();

//        executeService.execute(message);
//}

}
