package ru.acuma.shuffler.aspect.interceptor;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.model.constant.ExceptionCause;
import ru.acuma.shuffler.context.cotainer.Render;
import ru.acuma.shuffler.model.wrapper.RenderEvent;
import ru.acuma.shuffler.util.AspectUtil;

import java.util.Objects;

@Aspect
@Order(0)
@Component
@RequiredArgsConstructor
public class SweepMessageAspect {

    private final ApplicationEventPublisher eventPublisher;

    @Before("@annotation(ru.acuma.shuffler.aspect.marker.SweepMessage)")
    public void sweepMessage(final JoinPoint joinPoint) {
        var message = AspectUtil.extractMessage(joinPoint)
            .orElseThrow(() -> new DataException(ExceptionCause.MESSAGE_NOT_FOUND, "?"));
        if (Objects.isNull(message)) {
            return;
        }
        var event = RenderEvent.builder()
            .chatId(message.getChatId())
            .render(Render.forDelete(message.getMessageId()))
            .build();

        eventPublisher.publishEvent(event);
    }
}
