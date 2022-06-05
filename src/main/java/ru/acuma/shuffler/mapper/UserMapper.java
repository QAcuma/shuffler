package ru.acuma.shuffler.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.tables.pojos.UserInfo;

import java.time.OffsetDateTime;

@Component
public class UserMapper extends BaseMapper {

    private final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public UserInfo toUserInfo(User user) {
        if (user.getUserName() == null) {
            user.setUserName(String.valueOf(user.getId()));
        }
        mapperFactory.classMap(User.class, UserInfo.class)
                .field("id", "telegramId")
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        UserInfo userInfo = mapper.map(user, UserInfo.class);
        userInfo.setIsBlocked(Boolean.FALSE)
                .setCreatedAt(OffsetDateTime.now());
        return userInfo;
    }
}
