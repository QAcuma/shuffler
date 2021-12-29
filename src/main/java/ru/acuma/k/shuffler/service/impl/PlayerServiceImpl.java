package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffler.dao.PlayerDao;
import ru.acuma.k.shuffler.model.entity.KickerEvent;
import ru.acuma.k.shuffler.model.entity.KickerPlayer;
import ru.acuma.k.shuffler.service.PlayerService;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerDao playerDao;

    @Override
    public void authenticate(KickerEvent event, User user) {
        if (playerDao.isActive(user.getId())) {
        }

    }

    private KickerPlayer registerPlayer(KickerEvent event, User user) {

        return new KickerPlayer();
    }

//    private boolean isPresent ()


}
