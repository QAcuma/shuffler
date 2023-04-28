package ru.acuma.shuffler.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import ru.acuma.shufflerlib.model.Discipline;

import java.util.Arrays;

@UtilityClass
public final class ArgumentUtil {

    public static String extractParam(final String param, final String... args) {
        return Arrays.stream(args)
            .map(StringUtils::deleteWhitespace)
            .filter(arg -> StringUtils.startsWith(arg, param))
            .findFirst()
            .orElse(Discipline.KICKER.toString());
    }

}
