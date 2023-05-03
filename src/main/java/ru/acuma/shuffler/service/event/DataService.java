package ru.acuma.shuffler.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.acuma.shuffler.context.EventContext;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataService {

    private final EventContext eventContext;

    @Transactional
    public void saveData(final Long chatId) {
        Optional.ofNullable(eventContext.findEvent(chatId))
            .ifPresent(event -> {
//                    var eventEntity = eventMapper.toEvent(event, seasonService.getCurrentSeason().getId());
//                    eventRepository.save(eventEntity);
                }
            );
    }

}
