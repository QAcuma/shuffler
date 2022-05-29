package ru.acuma.k.shuffler.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class BaseMapper {

    protected final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    {
        mapperFactory.getConverterFactory().registerConverter(new DateTimeConverter());
    }

}
