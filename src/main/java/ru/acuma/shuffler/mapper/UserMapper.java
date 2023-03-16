package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.acuma.shuffler.model.dto.TgUserInfo;
import ru.acuma.shuffler.model.entity.UserInfo;

import java.time.OffsetDateTime;

@Mapper(
    config = MapperConfiguration.class
)
public abstract class UserMapper {

    @Mapping(target = "telegramId", source = "id")
    public abstract TgUserInfo toUserInfo(UserInfo source);

}
