package ru.acuma.shuffler.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdleException.class)
    public void handleIdle(IdleException exception) {
        log.debug(exception.getMessage());
    }

    @ExceptionHandler(DataException.class)
    public void handleIdle(DataException exception) {
        log.debug(exception.getMessage());
    }

}
