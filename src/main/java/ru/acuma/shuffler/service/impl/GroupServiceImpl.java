package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import ru.acuma.shuffler.mapper.GroupMapper;
import ru.acuma.shuffler.service.GroupService;
import ru.acuma.shufflerlib.dao.GroupDao;

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
