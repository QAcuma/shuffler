package ru.acuma.k.shuffler.service;

import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerGame;

public interface GameService {

    void buildGame(KickerEvent event);

}