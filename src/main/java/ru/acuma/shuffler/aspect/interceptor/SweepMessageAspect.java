package ru.acuma.shuffler.aspect.interceptor;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.service.message.MaintenanceService;
import ru.acuma.shuffler.util.AspectUtil;

import javax.ws.rs.NotFoundException;
import java.util.Objects;

@Aspect
@Order(0)
@Component
@RequiredArgsConstructor
public class SweepMessageAspect {

    private final MaintenanceService maintenanceService;

    @Before("@annotation(ru.acuma.shuffler.aspect.marker.SweepMessage)")
    public void sweepMessage(JoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint).orElseThrow(NotFoundException::new);
        if (Objects.isNull(message)) {
            return;
        }

        maintenanceService.sweepMessage(message);
    }
}
