package com.example.web.news.injections.mapper;

import com.example.web.news.storage.mapper.RowMapperMassage;
import org.springframework.jdbc.core.RowMapper;

public interface MapperMassage<MassageEntity> extends RowMapper<MassageEntity> {
    RowMapperMassage createData();
}
