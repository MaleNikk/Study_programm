package com.example.web.news.storage.mapper;

import com.example.web.news.entity.CommentEntity;
import com.example.web.news.injections.mapper.MapperComment;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@RequiredArgsConstructor
@Component
public class RowMapperComment implements MapperComment<CommentEntity> {
    @Override
    public CommentEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CommentEntity(
                rs.getLong(CommentEntity.Fields.id),
                rs.getLong(CommentEntity.Fields.id_news),
                rs.getLong(CommentEntity.Fields.pin_reader),
                rs.getString(CommentEntity.Fields.author),
                rs.getString(CommentEntity.Fields.text),
                rs.getString(CommentEntity.Fields.date_comment));
    }

    @Override
    public RowMapperComment createData() {
        return new RowMapperComment();
    }
}
