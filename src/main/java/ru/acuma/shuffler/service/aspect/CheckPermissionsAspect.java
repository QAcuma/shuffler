package ru.acuma.shuffler.service.aspect;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.cache.EventContext;
import ru.acuma.shuffler.util.AspectUtil;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckPermissionsAspect {

    private static final Long ID = 285250417L;

    private final EventContext eventContext;

    @Around("@annotation(CheckPlayerInEvent)")
    @SneakyThrows
    public Object inEventCheck(ProceedingJoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint).orElseThrow(NotFoundException::new);
        var event = eventContext.findEvent(message.getChatId());
        if (event == null || event.playerNotParticipate(message.getFrom().getId())) {
            return Optional.empty();
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(CheckPlayerNotInEvent)")
    @SneakyThrows
    public Object notInEventCheck(ProceedingJoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint).orElseThrow(NotFoundException::new);
        var event = eventContext.findEvent(message.getChatId());
        if (event == null || !event.playerNotParticipate(message.getFrom().getId())) {
            return Optional.empty();
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(CheckNoActiveEvent)")
    @SneakyThrows
    public Object noActiveEventCheck(ProceedingJoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint).orElseThrow(NotFoundException::new);
        if (eventContext.isActive(message.getChatId())) {
            return Optional.empty();
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(CheckUserIsAdmin)")
    @SneakyThrows
    public Object userIsAdminCheck(ProceedingJoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint).orElseThrow(NotFoundException::new);
        if (message.getFrom().getId().equals(ID)) {
            return Optional.empty();
        }

        return joinPoint.proceed();
    }

}
