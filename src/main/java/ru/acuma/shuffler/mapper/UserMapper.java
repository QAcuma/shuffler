package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.tables.pojos.UserInfo;

import java.time.OffsetDateTime;

@Mapper(uses = OffsetDateTimeMapper.class,
        componentModel = "spring",
        imports = OffsetDateTime.class)
public abstract class UserMapper {

    @Mapping(source = "id", target = "telegramId")
    @Mapping(target = "isBlocked", constant = "false")
    @Mapping(target = "createdAt", expression = "java(OffsetDateTime.now())")
    public abstract UserInfo toUserInfo(User source);

}
