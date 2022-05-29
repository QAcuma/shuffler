package ru.acuma.k.shuffler.mapper.converters;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeConverter extends CustomConverter<LocalDateTime, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(LocalDateTime source, Type<? extends OffsetDateTime> destinationType, MappingContext mappingContext) {
        return OffsetDateTime.from(ZonedDateTime.of(source, ZoneId.systemDefault()));
    }

}
