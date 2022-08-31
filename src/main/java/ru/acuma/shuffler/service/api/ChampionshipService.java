package ru.acuma.shuffler.service.api;

import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.acuma.shuffler.model.entity.TgEvent;

public interface ChampionshipService {


    void cancelChampionship(AbsSender absSender, TgEvent event);

    void finishChampionship(AbsSender absSender, TgEvent event);
}
