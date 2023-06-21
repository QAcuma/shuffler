package ru.acuma.shuffler.service.season;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.acuma.shuffler.context.cotainer.Render;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.constant.messages.MessageType;
import ru.acuma.shuffler.model.entity.GroupInfo;
import ru.acuma.shuffler.model.entity.Rating;
import ru.acuma.shuffler.model.entity.Season;
import ru.acuma.shuffler.repository.GroupInfoRepository;
import ru.acuma.shuffler.repository.RatingRepository;
import ru.acuma.shuffler.service.telegram.ExecuteService;

import java.util.Arrays;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class BroadcastService {

    private final GroupInfoRepository groupInfoRepository;
    private final TextBuilderService textBuilderService;
    private final RatingRepository ratingRepository;
    private final ExecuteService executeService;

    @Transactional
    public void seasonResultBroadcast(final Season season) {
        groupInfoRepository.findAllByIsActiveTrue()
            .forEach(groupInfo -> seasonResultBroadcast(season, groupInfo));
    }

    private void seasonResultBroadcast(final Season season, final GroupInfo groupInfo) {
        Arrays.stream(Discipline.values())
            .forEach(discipline -> seasonResultBroadcast(season, groupInfo, discipline));
    }

    private void seasonResultBroadcast(final Season season, final GroupInfo groupInfo, final Discipline discipline) {
        var seasonPlayers = ratingRepository.findALlSeasonRatings(season, groupInfo, discipline)
            .stream()
            .sorted(Comparator.comparingLong(Rating::getScore).reversed())
            .toList();

        if (seasonPlayers.isEmpty()) {
            return;
        }

        var message = SendMessage.builder()
            .chatId(String.valueOf(groupInfo.getId()))
            .parseMode(ParseMode.MARKDOWNV2)
            .text(textBuilderService.buildSeasonReport(seasonPlayers))
            .build();

        executeService.execute(message, Render.forSend(MessageType.STAT));
    }
}
