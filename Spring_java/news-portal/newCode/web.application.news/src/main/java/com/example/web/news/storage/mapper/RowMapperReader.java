package com.example.web.news.storage.mapper;

import com.example.web.news.entity.ReaderEntity;
import com.example.web.news.injections.mapper.MapperReader;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@RequiredArgsConstructor
@Component
public class RowMapperReader implements MapperReader<ReaderEntity> {
    @Override
    public ReaderEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ReaderEntity(
                rs.getLong(ReaderEntity.Fields.id),
                rs.getLong(ReaderEntity.Fields.pin),
                rs.getString(ReaderEntity.Fields.name),
                rs.getString(ReaderEntity.Fields.surname));
    }

    @Override
    public RowMapperReader createData() {
        return new RowMapperReader();
    }
}
