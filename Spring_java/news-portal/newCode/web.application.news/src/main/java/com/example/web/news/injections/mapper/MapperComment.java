package com.example.web.news.injections.mapper;

import com.example.web.news.storage.mapper.RowMapperComment;
import org.springframework.jdbc.core.RowMapper;

public interface MapperComment<CommentEntity> extends RowMapper<CommentEntity> {
    RowMapperComment createData();
}
