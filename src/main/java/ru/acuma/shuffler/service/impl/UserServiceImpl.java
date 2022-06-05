package ru.acuma.shuffler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.mapper.UserMapper;
import ru.acuma.shuffler.service.UserService;
import ru.acuma.shuffler.tables.pojos.UserInfo;
import ru.acuma.shufflerlib.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserInfo getUser(Long telegramId) {
        return userRepository.get(telegramId);
    }

    private Boolean hasAccess(Long telegramId) {
        return (userRepository.isActive(telegramId));
    }

    @Override
    public boolean authenticate(User user) {
        if (hasAccess(user.getId())) {
            return true;
        } else if (!userRepository.isBlocked(user.getId())) {
            userRepository.save(userMapper.toUserInfo(user));
            return true;
        }
        return false;
    }
}
