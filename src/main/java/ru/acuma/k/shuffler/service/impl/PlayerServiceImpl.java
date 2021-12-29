package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.cache.EventContextServiceImpl;
import ru.acuma.k.shuffler.model.domain.Player;
import ru.acuma.k.shuffler.service.PlayerService;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final EventContextServiceImpl eventContextService;

    @Override
    public Player authenticate(User user) {

        return new Player();
    }

//    private boolean isPresent ()


}
