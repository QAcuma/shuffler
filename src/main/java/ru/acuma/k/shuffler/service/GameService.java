package ru.acuma.k.shuffler.service;

import ru.acuma.k.shuffler.model.domain.KickerEvent;
import ru.acuma.k.shuffler.model.domain.KickerGame;

public interface GameService {

    KickerGame buildGame(KickerEvent event);

}
