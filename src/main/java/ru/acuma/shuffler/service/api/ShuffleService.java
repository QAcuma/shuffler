package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.entity.TgEvent;
import ru.acuma.shuffler.model.entity.TgEventPlayer;

import java.util.List;

public interface ShuffleService {

    List<TgEventPlayer> shuffle(TgEvent event);
}
