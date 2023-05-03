package ru.acuma.shuffler.service.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.mapper.UserMapper;
import ru.acuma.shuffler.model.constant.AuthStatus;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.model.entity.UserInfo;
import ru.acuma.shuffler.repository.UserInfoRepository;
import ru.acuma.shuffler.util.TimeMachine;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements Authenticatable<Long> {
    private final UserInfoRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserInfo getUser(final Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new DataException(ExceptionCause.USER_NOT_FOUND, userId));
    }

    @Transactional
    public void signUp(final User user) {
        var userInfo = userMapper.toUserInfo(user);
        userRepository.save(userInfo);
    }

    @Transactional
    public void update(final User user) {
        var userInfo = getUser(user.getId());
        userMapper.mergeUserInfo(userInfo, user);
    }

    @Transactional(readOnly = true)
    public List<UserInfo> getActiveUsers() {
        return userRepository.findAllByIsActiveTrue();
    }

    @Transactional
    public void deleteUser(Long userId) {
        getUser(userId).setDeletedAt(TimeMachine.localDateTimeNow());
    }

    @Transactional
    public boolean signIn(User user) {
        var userInfo = getUser(user.getId());
        userMapper.mergeUserInfo(userInfo, user);

        return userInfo.getIsActive();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthStatus authenticate(final Long userId) {
        return userRepository.findForAuthById(userId)
            .map(info -> Boolean.TRUE.equals(info.getIsActive())
                         ? AuthStatus.SUCCESS
                         : AuthStatus.DENY)
            .orElse(AuthStatus.UNREGISTERED);
    }
}
