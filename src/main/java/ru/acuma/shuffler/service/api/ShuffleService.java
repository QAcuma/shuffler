package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.domain.TgEvent;
import ru.acuma.shuffler.model.domain.TgEventPlayer;

import java.util.List;

public interface ShuffleService {

    List<TgEventPlayer> shuffle(TgEvent event);
}
