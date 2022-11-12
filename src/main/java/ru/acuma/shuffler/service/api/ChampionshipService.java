package ru.acuma.shuffler.service.api;

import ru.acuma.shuffler.model.entity.TgEvent;

public interface ChampionshipService {


    void finishEvent(TgEvent event);

    void finishChampionship(TgEvent event);
}
