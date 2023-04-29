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
public abstract class GroupMapper {

    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "name", ignore = true)
    public abstract GroupInfo toGroupInfo(Chat source);

    @Transactional(propagation = Propagation.MANDATORY)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "name", ignore = true)
    public abstract void mergeGroupInfo(@MappingTarget GroupInfo groupInfo, Chat chat);

}
