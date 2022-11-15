package ru.acuma.shuffler.util;

import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public class AspectUtil {

    public static Optional<Message> extractMessage(JoinPoint joinPoint) {
        var args = joinPoint.getArgs();

        return Arrays.stream(args)
                .filter(Message.class::isInstance)
                .map(Message.class::cast)
                .findFirst();
    }


}
