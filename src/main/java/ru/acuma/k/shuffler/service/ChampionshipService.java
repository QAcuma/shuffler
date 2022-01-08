package ru.acuma.k.shuffler.service;

import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.k.shuffler.model.entity.KickerEvent;

public interface ChampionshipService {


    void cancelChampionship(AbsSender absSender, KickerEvent event);

    void finishChampionship(AbsSender absSender, KickerEvent event);
}
