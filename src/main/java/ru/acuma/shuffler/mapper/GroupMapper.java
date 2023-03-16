package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.telegram.telegrambots.meta.api.objects.Chat;
import ru.acuma.shuffler.model.entity.GroupInfo;

@Mapper(
    config = MapperConfiguration.class
)
public abstract class GroupMapper {

    @Mapping(target = "isBlocked", constant = "false")
    @Mapping(target = "name", ignore = true)
    public abstract GroupInfo toGroupInfo(Chat source);

}
