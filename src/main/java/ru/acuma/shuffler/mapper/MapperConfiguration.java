package ru.acuma.shuffler.mapper;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;

@MapperConfig(
    componentModel = "spring",
    builder = @Builder(
        disableBuilder = true
    ),
    uses = OffsetDateTimeMapper.class,
    injectionStrategy = InjectionStrategy.FIELD,
    implementationPackage = "<PACKAGE_NAME>.impl"
)
public interface MapperConfiguration {
}
