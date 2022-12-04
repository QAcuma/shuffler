package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.telegram.telegrambots.meta.api.objects.Chat;
import ru.acuma.shuffler.tables.pojos.GroupInfo;

@Mapper(componentModel = "spring")
public abstract class GroupMapper {

    @Mapping(source = "id", target = "chatId")
    @Mapping(target = "isBlocked", constant = "false")
    public abstract GroupInfo toGroupInfo(Chat source);

}
