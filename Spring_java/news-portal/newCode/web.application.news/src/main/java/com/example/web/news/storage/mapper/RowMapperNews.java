package com.example.web.news.storage.mapper;

import com.example.web.news.entity.NewsEntity;
import com.example.web.news.injections.mapper.MapperNews;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@RequiredArgsConstructor
@Component
public class RowMapperNews implements MapperNews<NewsEntity> {

    public NewsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new NewsEntity(
                rs.getLong(NewsEntity.Fields.id),
                rs.getLong(NewsEntity.Fields.category_id),
                rs.getString(NewsEntity.Fields.title),
                rs.getString(NewsEntity.Fields.news),
                rs.getString(NewsEntity.Fields.author),
                rs.getString(NewsEntity.Fields.date_news));
    }
    @Override
    public RowMapperNews createData(){
        return new RowMapperNews();
    }
}
