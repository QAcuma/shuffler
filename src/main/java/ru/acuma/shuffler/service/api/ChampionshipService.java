package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.dto.TgEvent;

public interface ChampionshipService {

    void finishEvent(TgEvent event);

    void finishChampionship(TgEvent event);
}
