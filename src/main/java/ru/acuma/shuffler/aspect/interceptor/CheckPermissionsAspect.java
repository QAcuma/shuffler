package ru.acuma.shuffler.aspect.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.context.EventContext;
import ru.acuma.shuffler.util.AspectUtil;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckPermissionsAspect {

    @Value("${application.users.root}")
    private List<Long> rootUsers;

    private final EventContext eventContext;

    @Around("@annotation(ru.acuma.shuffler.aspect.marker.CheckPlayerInEvent)")
    @SneakyThrows
    public Object inEventCheck(ProceedingJoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint).orElseThrow(NotFoundException::new);
        var event = eventContext.findEvent(message.getChatId());
        if (event == null || event.playerNotParticipate(message.getFrom().getId())) {
            return Optional.empty();
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(ru.acuma.shuffler.aspect.marker.CheckPlayerNotInEvent)")
    @SneakyThrows
    public Object notInEventCheck(ProceedingJoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint).orElseThrow(NotFoundException::new);
        var event = eventContext.findEvent(message.getChatId());
        if (event == null || !event.playerNotParticipate(message.getFrom().getId())) {
            return Optional.empty();
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(ru.acuma.shuffler.aspect.marker.CheckNoActiveEvent)")
    @SneakyThrows
    public Object noActiveEventCheck(ProceedingJoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint).orElseThrow(NotFoundException::new);
        if (eventContext.isActive(message.getChatId())) {
            return Optional.empty();
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(ru.acuma.shuffler.aspect.marker.CheckUserIsAdmin)")
    @SneakyThrows
    public Object userIsAdminCheck(ProceedingJoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint).orElseThrow(NotFoundException::new);
        if (rootUsers.contains(message.getFrom().getId())) {
            return Optional.empty();
        }

        return joinPoint.proceed();
    }

}
