package ru.acuma.shuffler.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@Component
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void handle(Exception exception) {
        if (exception instanceof IdleException idleException) {
            handle(idleException);
            return;
        }

        if (exception instanceof DataException dataException) {
            handle(dataException);
            return;
        }

        log.error(exception.getMessage());
        exception.printStackTrace();
    }

    private void handle(IdleException exception) {
        log.debug(exception.getMessage());
    }

    private void handle(DataException exception) {
        log.error(exception.getMessage());
        exception.printStackTrace();
    }
}
