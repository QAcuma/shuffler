package ru.acuma.shuffler.service.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.service.api.GroupService;
import ru.acuma.shuffler.service.api.UserService;
import ru.acuma.shuffler.util.AspectUtil;

import javax.ws.rs.NotFoundException;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthAspect {

    private final UserService userService;
    private final GroupService groupService;

    @Before("@annotation(UserAuth)")
    public void authUser(JoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint).orElseThrow(NotFoundException::new);
        userService.authenticate(message.getFrom());
    }

    @Before("@annotation(GroupAuth)")
    public void authChat(JoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint).orElseThrow(NotFoundException::new);
        groupService.authenticate(message.getChat());
    }


}
