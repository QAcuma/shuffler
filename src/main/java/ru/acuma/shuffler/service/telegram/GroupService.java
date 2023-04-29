package ru.acuma.shuffler.service.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Chat;
import ru.acuma.shuffler.mapper.GroupMapper;
import ru.acuma.shuffler.model.entity.GroupInfo;
import ru.acuma.shuffler.repository.GroupInfoRepository;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupMapper groupMapper;
    private final GroupInfoRepository groupInfoRepository;

    @Transactional
    public GroupInfo getGroupInfo(final Long chatId) {
        return groupInfoRepository.findById(chatId)
            .orElseGet(() -> signUpGroup(chatId));
    }

    @Transactional
    public boolean signIn(Chat chat) {
        if (!(chat.isGroupChat() || chat.isSuperGroupChat())) {
            return false;
        }
        var groupInfo = getGroupInfo(chat.getId());
        groupMapper.mergeGroupInfo(groupInfo, chat);

        return groupInfo.getIsActive();
    }

    private GroupInfo signUpGroup(final Long chatId) {
        var groupInfo = GroupInfo.builder()
            .id(chatId)
            .isActive(Boolean.TRUE)
            .build();

        return groupInfoRepository.save(groupInfo);

    }
}
