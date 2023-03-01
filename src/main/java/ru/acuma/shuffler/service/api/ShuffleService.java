package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.dto.TgEvent;
import ru.acuma.shuffler.model.dto.TgEventPlayer;

import java.util.List;

public interface ShuffleService {

    List<TgEventPlayer> shuffle(TgEvent event);
}
