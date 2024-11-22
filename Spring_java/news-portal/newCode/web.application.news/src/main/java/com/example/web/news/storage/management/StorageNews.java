package com.example.web.news.storage.management;

import com.example.web.news.entity.NewsEntity;
import com.example.web.news.exception.SearchException;
import com.example.web.news.injections.management.ManagementNews;
import com.example.web.news.injections.mapper.MapperNews;
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

@Repository
@Primary
@RequiredArgsConstructor
@Slf4j
public class StorageNews implements ManagementNews<NewsEntity> {

    private final JdbcTemplate jdbcTemplate;
    @Getter
    private final HashSet<Long> numbersId;

    private final MapperNews<NewsEntity> mapperNews;

    @Override
    public List<NewsEntity> searchAll() {
        log.info("Enjoy StorageManagementParent method search all.");
        String sql = "SELECT * FROM news";
        List<NewsEntity> find = jdbcTemplate.query(sql, mapperNews.createData());
        find.forEach(id -> getNumbersId().add(id.getId()));
        return find;
    }

    @Override
    public NewsEntity searchById(Long id) {
        log.info("Enjoy StorageManagementParent method search by id: {}.", id);
        String sql = "SELECT * FROM news WHERE id = ?";
        return DataAccessUtils.singleResult(
                jdbcTemplate.query(sql,
                        new ArgumentPreparedStatementSetter(new Object[]{id}),
                        new RowMapperResultSetExtractor<>(mapperNews.createData(), 1)));
    }

    @Override
    public NewsEntity save(NewsEntity newsEntity) {
        getNumbersId().add(newsEntity.getId());
        log.info("Enjoy StorageManagementParent method save newsEntity: {}.", newsEntity);
        String sql = "INSERT INTO news (id, category_id, title, news, author, date_news) VALUES(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                newsEntity.getId(),
                newsEntity.getCategory_id(),
                newsEntity.getTitle(),
                newsEntity.getNews(),
                newsEntity.getAuthor(),
                newsEntity.getDate_news());
        return newsEntity;
    }

    @Override
    public NewsEntity edit(NewsEntity newsEntity) {
        log.info("Enjoy StorageManagementParent method edit newsEntity: {}.", newsEntity);
        NewsEntity newsEntityEdited = searchById(newsEntity.getId());
        if (newsEntityEdited != null) {
            String sql = "UPDATE news SET category_id = ?, title = ?, news = ?, author = ?, date_news = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    newsEntity.getCategory_id(),
                    newsEntity.getTitle(),
                    newsEntity.getNews(),
                    newsEntity.getAuthor(),
                    newsEntity.getDate_news(),
                    newsEntity.getId());
            return newsEntity;
        }
        log.info("NewsEntity {} for edited not fount", newsEntity);
        throw new SearchException(MessageFormat.format("NewsEntity: {0} are not present in data base!", newsEntity));
    }

    @Override
    public void removeById(Long id) {
        log.info("Enjoy StorageManagementParent method remove by id: {}.", id);
        String sql = "DELETE FROM news WHERE id = ?";
        jdbcTemplate.update(sql, id);
        getNumbersId().remove(id);
    }

    @Override
    public void batchInsert(List<NewsEntity> newEntities) {
        log.info("Enjoy StorageManagementParent method batchInsert.");
        String sql = "INSERT INTO news (id,category_id, title, news, author, date_news) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                NewsEntity newsEntity = newEntities.get(i);
                ps.setLong(1, newsEntity.getId());
                ps.setLong(2,newsEntity.getCategory_id());
                ps.setString(3, newsEntity.getTitle());
                ps.setString(4, newsEntity.getNews());
                ps.setString(5, newsEntity.getAuthor());
                ps.setString(6, String.valueOf(newsEntity.getDate_news()));
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
        log.info("Enjoy StorageManagementParent method numbersId.");
        String sql = "SELECT * FROM news";
        for (NewsEntity newsEntity :jdbcTemplate.query(sql, mapperNews.createData())){
            getNumbersId().add(newsEntity.getId());
        }
        return numbersId;
    }
}
