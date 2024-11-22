package com.example.web.news.storage.mapper;


import com.example.web.news.entity.NewsEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@RequiredArgsConstructor
@Component
public class NewsRowMapper implements RowMapper<NewsEntity> {

    @Override
    public NewsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setId(rs.getLong(NewsEntity.Fields.id));
        newsEntity.setCategory_id(rs.getLong(NewsEntity.Fields.category_id));
        newsEntity.setTitle(rs.getString(NewsEntity.Fields.title));
        newsEntity.setNews(rs.getString(NewsEntity.Fields.news));
        newsEntity.setAuthor(rs.getString(NewsEntity.Fields.author));
        newsEntity.setDate_news(rs.getString(NewsEntity.Fields.date_news));
        return newsEntity;
    }
}
