package ru.acuma.shuffler.service;

import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.model.entity.GameEvent;

public interface ChampionshipService {


    void cancelChampionship(AbsSender absSender, GameEvent event);

    void finishChampionship(AbsSender absSender, GameEvent event);
}
