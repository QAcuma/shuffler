package ru.acuma.k.shuffler.service;

import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerEventPlayer;

import java.util.List;

public interface ShuffleService {

    List<KickerEventPlayer> shuffle(KickerEvent event);
}
