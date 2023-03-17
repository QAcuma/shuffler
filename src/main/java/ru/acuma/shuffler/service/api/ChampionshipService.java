package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.domain.TgEvent;

public interface ChampionshipService {

    void finishEvent(TgEvent event);

    void finishChampionship(TgEvent event);
}
