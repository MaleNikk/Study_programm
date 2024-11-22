package com.example.web.news.storage.management;


import com.example.web.news.entity.CategoryEntity;
import com.example.web.news.entity.NewsEntity;
import com.example.web.news.exception.SearchException;
import com.example.web.news.storage.mapper.CategoryRowMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Qualifier
@RequiredArgsConstructor
@Slf4j
public class StorageCategories implements ManagementCategories<CategoryEntity> {

    private final JdbcTemplate jdbcTemplate;
    @Getter
    private final HashSet<Long> numbersCategoriesId;

    private final ManagementNews<NewsEntity> newsManagement;

    @Override
    public List<CategoryEntity> searchAll() {
        log.info("Enjoy StorageCategories method search all.");
        String sql = "SELECT * FROM categories";
        List<CategoryEntity> find = jdbcTemplate.query(sql, new CategoryRowMapper());
        find.forEach(id -> getNumbersCategoriesId().add(id.getId()));
        return find;
    }

    @Override
    public CategoryEntity searchById(Long id) {
        log.info("Enjoy StorageCategories method search by id: {}.", id);
        String sql = "SELECT * FROM categories WHERE id = ?";
        return DataAccessUtils.singleResult(
                jdbcTemplate.query(sql,
                        new ArgumentPreparedStatementSetter(new Object[]{id}),
                        new RowMapperResultSetExtractor<>(new CategoryRowMapper(), 1)));
    }

    @Override
    public CategoryEntity save(CategoryEntity category) {
        getNumbersCategoriesId().add(category.getId());
        log.info("Enjoy StorageCategories method save newsEntity: {}.", category);
        String sql = "INSERT INTO categories (id, title) VALUES(?, ?)";
        jdbcTemplate.update(sql,
                category.getId(),
                category.getTitle());
        return category;
    }

    @Override
    public CategoryEntity edit(CategoryEntity category) {
        log.info("Enjoy StorageCategories method edit newsEntity: {}.", category);
        CategoryEntity categoryEdited = searchById(category.getId());
        if (categoryEdited != null) {
            String sql = "UPDATE categories SET title = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    category.getTitle(),
                    category.getId());
            return category;
        }
        log.info("NewsEntity {} for edited not fount", category);
        throw new SearchException(MessageFormat.format("Category: {0} are not present in data base!", category));
    }

    @Override
    public void removeById(Long id) {
        log.info("Enjoy StorageCategories method remove by id: {}.", id);
        String sql = "DELETE FROM categories WHERE id = ?";
        jdbcTemplate.update(sql, id);
        getNumbersCategoriesId().remove(id);
    }

    @Override
    public void batchInsert(List<CategoryEntity> categories) {
        log.info("Enjoy StorageCategories method batchInsert.");
        String sql = "INSERT INTO categories (id, title) VALUES(?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CategoryEntity category = categories.get(i);
                ps.setLong(1, category.getId());
                ps.setString(2, category.getTitle());
                getNumbersCategoriesId().add(category.getId());
            }
            @Override
            public int getBatchSize() {
                return categories.size();
            }
        });
    }
    @Override
    public HashSet<Long> numbersId() {
        log.info("Enjoy StorageCategories method numbersId.");
        String sql = "SELECT * FROM categories";
        for (CategoryEntity categoryEntity :jdbcTemplate.query(sql, new CategoryRowMapper())){
            getNumbersCategoriesId().add(categoryEntity.getId());
        }
        return numbersCategoriesId;
    }
    @Override
    public List<NewsEntity> newsCategory(Long id) {
        log.info("Enjoy StorageCategories method newsCategory.");
        return newsManagement.searchAll().stream().
                filter(news->id == news.getCategory_id()).toList();
    }
}