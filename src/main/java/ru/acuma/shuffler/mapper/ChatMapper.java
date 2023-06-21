package ru.acuma.shuffler.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Chat;
import ru.acuma.shuffler.model.entity.GroupInfo;

@Mapper(
    config = MapperConfiguration.class
)
public interface ChatMapper {

    @Mapping(target = "id", source = "chat.id")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "name", source = "chatName")
    @Mapping(target = "title", source = "chat.title")
    GroupInfo toGroupInfo(Chat chat, String chatName);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "chat.title")
    @Mapping(target = "name", source = "name")
    @Transactional(propagation = Propagation.MANDATORY)
    void updateGroupInfo(@MappingTarget GroupInfo groupInfo, Chat chat, String name);

}
