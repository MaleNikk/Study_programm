package com.example.web.news.storage;

import com.example.web.news.entity.NewsEntity;
import com.example.web.news.exception.SearchException;
import com.example.web.news.storage.mapper.NewsRowMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
@Slf4j
public class DataBaseNews implements ContactNews {

    private final JdbcTemplate jdbcTemplate;
    @Getter
    private final HashSet<Long> numbersId;

    @Override
    public List<NewsEntity> searchAll() {
        log.debug("Enjoy DataBaseNews method search all.");
        String sql = "SELECT * FROM news";
        List<NewsEntity> find = jdbcTemplate.query(sql, new NewsRowMapper());
        find.forEach(id -> getNumbersId().add(id.getId()));
        return find;
    }

    @Override
    public Optional<NewsEntity> searchNewsById(Long id) {
        log.debug("Enjoy DataBaseNews method search by id: {}.", id);
        String sql = "SELECT * FROM news WHERE id = ?";
        NewsEntity newsEntity = DataAccessUtils.singleResult(
                jdbcTemplate.query(sql,
                        new ArgumentPreparedStatementSetter(new Object[]{id}),
                        new RowMapperResultSetExtractor<>(new NewsRowMapper(), 1)));
        return Optional.ofNullable(newsEntity);
    }

    @Override
    public NewsEntity saveNews(NewsEntity newsEntity) {
        getNumbersId().add(newsEntity.getId());
        log.debug("Enjoy DataBaseNews method save newsEntity: {}.", newsEntity);
        String sql = "INSERT INTO news (id, title, news, author, date_news) VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                newsEntity.getId(),
                newsEntity.getTitle(),
                newsEntity.getNews(),
                newsEntity.getAuthor(),
                newsEntity.getDate_news());
        return newsEntity;
    }

    @Override
    public NewsEntity editNews(NewsEntity newsEntity) {
        log.debug("Enjoy DataBaseNews method edit newsEntity: {}.", newsEntity);
        NewsEntity newsEntityEdited = searchNewsById(newsEntity.getId()).orElse(null);
        if (newsEntityEdited != null) {
            String sql = "UPDATE news SET title = ?, news = ?, author = ?, date_news = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    newsEntity.getTitle(),
                    newsEntity.getNews(),
                    newsEntity.getAuthor(),
                    newsEntity.getDate_news(),
                    newsEntity.getId());
            return newsEntity;
        }
        log.warn("NewsEntity {} for edited not fount", newsEntity);
        throw new SearchException(MessageFormat.format("NewsEntity: {0} are not present in data base!", newsEntity));
    }

    @Override
    public void removeNewsById(Long id) {
        log.debug("Enjoy DataBaseNews method remove by id: {}.", id);
        String sql = "DELETE FROM news WHERE id = ?";
        jdbcTemplate.update(sql, id);
        getNumbersId().remove(id);
    }

    @Override
    public void batchInsert(List<NewsEntity> newEntities) {
        log.debug("Enjoy DataBaseNews method batchInsert.");
        String sql = "INSERT INTO news (id, title, news, author, date_news) VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                NewsEntity newsEntity = newEntities.get(i);
                ps.setLong(1, newsEntity.getId());
                ps.setString(2, newsEntity.getTitle());
                ps.setString(3, newsEntity.getNews());
                ps.setString(4, newsEntity.getAuthor());
                ps.setString(5, String.valueOf(newsEntity.getDate_news()));
                getNumbersId().add(newsEntity.getId());
            }

            @Override
            public int getBatchSize() {
                return newEntities.size();
            }
        });
    }

    @Override
    public HashSet<Long> numbersId() {
        log.debug("Enjoy DataBaseNews method numbersId.");
        String sql = "SELECT * FROM news";
        for (NewsEntity newsEntity :jdbcTemplate.query(sql, new NewsRowMapper())){
            getNumbersId().add(newsEntity.getId());
        }
        return numbersId;
    }
}
