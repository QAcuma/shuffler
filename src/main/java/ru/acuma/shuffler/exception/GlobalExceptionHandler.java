package ru.acuma.shuffler.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Component
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void handle(Exception exception) {
        log.error(exception.getMessage());
        exception.printStackTrace();
    }

    @ExceptionHandler(IdleException.class)
    public void handle(IdleException exception) {
        log.debug(exception.getMessage());
    }

    @ExceptionHandler(DataException.class)
    public void handle(DataException exception) {
        log.debug(exception.getMessage());
    }
}
