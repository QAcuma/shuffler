package ru.acuma.shuffler.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import ru.acuma.shuffler.exception.DataException;
import ru.acuma.shuffler.model.constant.ExceptionCause;

import java.util.Arrays;
import java.util.function.Function;

@UtilityClass
public final class ArgumentUtil {

    public static <R> R extractParam(final String param, final Function<String, R> stringExtractor, final String... args) {
        var value = Arrays.stream(args)
            .map(StringUtils::deleteWhitespace)
            .filter(arg -> StringUtils.startsWith(arg, param))
            .findFirst()
            .map(arg -> StringUtils.substringAfter(arg, "="))
            .orElseThrow(() -> new DataException(ExceptionCause.CANNOT_EXTRACT_PARAM, param));

        return stringExtractor.apply(value);
    }
}
