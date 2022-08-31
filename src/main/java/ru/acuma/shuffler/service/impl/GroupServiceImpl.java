package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import ru.acuma.shuffler.mapper.GroupMapper;
import ru.acuma.shuffler.service.api.GroupService;
import ru.acuma.shufflerlib.repository.GroupRepository;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupMapper groupMapper;
    private final GroupRepository groupRepository;

    @Override
    public boolean authenticate(Chat chat) {
        if (!(chat.isGroupChat() || chat.isSuperGroupChat())) {
            return false;
        }
        if (!groupRepository.isActive(chat.getId())) {
            groupRepository.save(groupMapper.toGroupInfo(chat));
            return true;
        }
        return true;
    }

}
