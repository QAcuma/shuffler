package ru.acuma.shuffler.service.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.service.api.MaintenanceService;
import ru.acuma.shuffler.util.AspectUtil;

import javax.ws.rs.NotFoundException;
import java.util.Objects;

@Aspect
@Order(0)
@Component
@RequiredArgsConstructor
public class SweepMessageAspect {

    private final MaintenanceService maintenanceService;

    @Before("@annotation(SweepMessage)")
    public void authChat(JoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint).orElseThrow(NotFoundException::new);
        if (Objects.isNull(message)) {
            return;
        }

        maintenanceService.sweepMessage(message);
    }

}
