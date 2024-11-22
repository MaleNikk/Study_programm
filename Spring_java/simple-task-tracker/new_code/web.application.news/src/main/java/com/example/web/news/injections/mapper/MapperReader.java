package com.example.web.news.injections.mapper;

import com.example.web.news.storage.mapper.RowMapperReader;
import org.springframework.jdbc.core.RowMapper;

public interface MapperReader<ReaderEntity> extends RowMapper<ReaderEntity> {
    RowMapperReader createData();
}
