package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerGame;
import ru.acuma.k.shuffler.service.GameService;
import ru.acuma.k.shuffler.service.PlayerService;
import ru.acuma.k.shuffler.service.UserService;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final UserService userService;
    private final PlayerService playerService;

    @Override
    public KickerGame buildGame(KickerEvent event) {

        return null;
    }
}
