package ru.acuma.shuffler.mapper;

import ma.glasnost.orika.MapperFacade;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import ru.acuma.shuffler.tables.pojos.GroupInfo;

@Component
public class GroupMapper extends BaseMapper {

    public GroupInfo toGroupInfo(Chat chat) {
        mapperFactory.classMap(Chat.class, GroupInfo.class)
                .field("id", "chatId")
                .byDefault()
                .register();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        GroupInfo groupInfo = mapper.map(chat, GroupInfo.class);
        groupInfo.setIsBlocked(Boolean.FALSE);
        return groupInfo;
    }

}
