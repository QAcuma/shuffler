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
public abstract class ChatMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "title", source = "title")
    public abstract GroupInfo toGroupInfo(Chat chat);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "name", source = "description")
    @Transactional(propagation = Propagation.MANDATORY)
    public abstract void updateGroupInfo(@MappingTarget GroupInfo groupInfo, Chat chat);

}
