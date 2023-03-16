package ru.acuma.shuffler.service.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.mapper.GroupMapper;
import ru.acuma.shuffler.model.entity.GroupInfo;
import ru.acuma.shuffler.model.enums.ExceptionCause;
import ru.acuma.shuffler.repository.GroupInfoRepository;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupMapper groupMapper;
    private final GroupInfoRepository groupInfoRepository;

    public GroupInfo getGroupInfo(Long chatId) {
        return groupInfoRepository.findById(chatId)
            .orElseThrow(() -> new DataException(ExceptionCause.GROUP_NOT_FOUND, chatId));
    }

    public boolean signIn(Chat chat) {
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
