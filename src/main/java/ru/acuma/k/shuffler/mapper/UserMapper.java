package ru.acuma.k.shuffle.mapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.k.shuffle.tables.pojos.UserInfo;

@Component
public class UserMapper {

    private final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public UserInfo toUserInfo(User user) {
        mapperFactory.classMap(User.class, UserInfo.class)
                .field("id", "telegramId")
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        UserInfo userInfo = mapper.map(user, UserInfo.class);
        userInfo.setIsBlocked(Boolean.FALSE);
        return userInfo;
    }


}