package com.example.web.news.storage.mapper;

import com.example.web.news.entity.CategoryEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@RequiredArgsConstructor
@Component
public class CategoryRowMapper implements RowMapper<CategoryEntity> {
    @Override
    public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(rs.getLong(CategoryEntity.Fields.id));
        categoryEntity.setTitle(rs.getString(CategoryEntity.Fields.title));
        return categoryEntity;
    }
}
