package ru.acuma.shuffler.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum YearSeason {
    WINTER(List.of(12, 1, 2)),
    SPRING(List.of(3, 4, 5)),
    SUMMER(List.of(6, 7, 8)),
    AUTUMN(List.of(9, 10, 11));

    private final List<Integer> months;

    public static YearSeason getByMonthNumber(Integer monthNumber) {
        return Arrays.stream(values())
            .filter(month -> month.getMonths().contains(monthNumber))
            .findFirst()
            .orElseThrow(() -> new EnumConstantNotPresentException(YearSeason.class, String.valueOf(monthNumber)));
    }
}
