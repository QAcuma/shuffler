package ru.acuma.shuffler.aspect.interceptor;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.model.enums.ExceptionCause;
import ru.acuma.shuffler.service.telegram.GroupService;
import ru.acuma.shuffler.service.user.UserService;
import ru.acuma.shuffler.util.AspectUtil;

import javax.ws.rs.NotFoundException;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthAspect {

    private final UserService userService;
    private final GroupService groupService;

    @Before("@annotation(ru.acuma.shuffler.aspect.marker.UserAuth)")
    public void authUser(JoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint).orElseThrow(NotFoundException::new);
        boolean signedIn = userService.signIn(message.getFrom());
        if (!signedIn) {
            throw new DataException(ExceptionCause.USER_IS_NOT_ACTIVE, message.getFrom().getId());
        }
    }

    @Before("@annotation(ru.acuma.shuffler.aspect.marker.GroupAuth)")
    public void authChat(JoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint).orElseThrow(NotFoundException::new);
        boolean signedIn = groupService.signIn(message.getChat());
        if (!signedIn) {
            throw new DataException(ExceptionCause.CHAT_IS_NOT_ACTIVE, message.getChat().getId());
        }
    }
}
