package ru.acuma.shuffler.mapper;

import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public abstract class OffsetDateTimeMapper {

    public OffsetDateTime map(LocalDateTime source) {
        return source == null
                ? null
                : source.atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }

}
