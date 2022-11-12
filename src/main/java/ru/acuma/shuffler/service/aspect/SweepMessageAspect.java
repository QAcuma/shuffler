package ru.acuma.shuffler.service.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.acuma.shuffler.service.api.MaintenanceService;

import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class SweepMessageAspect {

    private final MaintenanceService maintenanceService;

    @Before("@annotation(SweepMessage)")
    public void authChat(JoinPoint joinPoint) {
        var message = extractMessage(joinPoint);
        if (Objects.isNull(message)) {
            return;
        }

        maintenanceService.sweepMessage(message);
    }

    private Message extractMessage(JoinPoint joinPoint) {
        var args = joinPoint.getArgs();

        return Arrays.stream(args)
                .filter(Message.class::isInstance)
                .map(Message.class::cast)
                .findFirst()
                .orElse(null);
    }

}
