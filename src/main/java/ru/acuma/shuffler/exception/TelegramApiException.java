package ru.acuma.shuffler.exception;

import lombok.Getter;
import ru.acuma.shuffler.model.constant.ExceptionCause;

public class TelegramApiException extends RuntimeException {

    private final String message;
    @Getter
    private final ExceptionCause exceptionCause;

    public TelegramApiException(ExceptionCause exceptionCause, Object... args) {
        this.exceptionCause = exceptionCause;
        message = exceptionCause.getDescription().formatted(args);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
