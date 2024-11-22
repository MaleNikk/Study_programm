package com.example.web.news.injections.mapper;

import com.example.web.news.storage.mapper.RowMapperNews;
import org.springframework.jdbc.core.RowMapper;

public interface MapperNews<NewsEntity> extends RowMapper<NewsEntity> {
    RowMapperNews createData();
}
