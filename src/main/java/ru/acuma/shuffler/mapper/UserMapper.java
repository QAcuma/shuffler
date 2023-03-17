package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.model.domain.TgUserInfo;
import ru.acuma.shuffler.model.entity.UserInfo;

@Mapper(
    config = MapperConfiguration.class,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class UserMapper {

    @Mapping(target = "telegramId", source = "id")
    public abstract TgUserInfo toUserInfo(UserInfo source);

    @Transactional(propagation = Propagation.MANDATORY)
    public abstract void mergeUserInfo(@MappingTarget UserInfo userInfo, User user);
}
