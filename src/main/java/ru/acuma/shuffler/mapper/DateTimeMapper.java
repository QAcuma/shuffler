package ru.acuma.shuffler.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static java.util.function.Predicate.not;

public interface DateTimeMapper {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @SuppressWarnings({"IllegalMethodCall"})
    static LocalDateTime mapToDateTime(String dateTime) {
        return Optional.ofNullable(dateTime)
            .filter(not(String::isBlank))
            .map(s -> LocalDateTime.parse(s, dateTimeFormatter))
            .orElse(null);
    }

    @SuppressWarnings({"IllegalMethodCall"})
    static LocalDate mapToDate(String date) {
        return Optional.ofNullable(date)
            .filter(not(String::isBlank))
            .map(s -> LocalDate.parse(s, dateFormatter))
            .orElse(null);
    }

    static String map(LocalDateTime dateTime) {
        return Optional.ofNullable(dateTime)
            .map(dateTimeFormatter::format)
            .orElse(null);
    }

    static String map(LocalDate date) {
        return Optional.ofNullable(date)
            .map(dateFormatter::format)
            .orElse(null);
    }
}
