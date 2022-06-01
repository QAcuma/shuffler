package ru.acuma.shuffler.service;

import ru.acuma.shuffler.model.entity.GameEvent;
import ru.acuma.shuffler.model.entity.GameEventPlayer;

import java.util.List;

public interface ShuffleService {

    List<GameEventPlayer> shuffle(GameEvent event);
}
