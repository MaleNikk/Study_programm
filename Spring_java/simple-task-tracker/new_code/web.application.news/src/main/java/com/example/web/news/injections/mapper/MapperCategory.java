package com.example.web.news.injections.mapper;

import com.example.web.news.storage.mapper.RowMapperCategory;
import org.springframework.jdbc.core.RowMapper;

public interface MapperCategory<CategoryEntity> extends RowMapper<CategoryEntity> {
    RowMapperCategory createData();
}
