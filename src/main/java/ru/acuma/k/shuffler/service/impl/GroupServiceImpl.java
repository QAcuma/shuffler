package ru.acuma.k.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import ru.acuma.k.shuffler.dao.GroupDao;
import ru.acuma.k.shuffler.mapper.GroupMapper;
import ru.acuma.k.shuffler.service.GroupService;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupMapper groupMapper;
    private final GroupDao groupDao;

    @Override
    public boolean authenticate(Chat chat) {
        if (!chat.isGroupChat()) {
            return false;
        }
        if (!groupDao.isActive(chat.getId())) {
            groupDao.save(groupMapper.toGroupInfo(chat));
            return true;
        }
        return true;
    }

}
