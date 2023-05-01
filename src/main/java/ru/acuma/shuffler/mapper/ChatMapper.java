package ru.acuma.shuffler.mapper;

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

    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Transactional(propagation = Propagation.MANDATORY)
    public abstract void mergeGroupInfo(@MappingTarget GroupInfo groupInfo, Chat chat);

}
