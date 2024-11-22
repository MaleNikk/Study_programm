package com.example.web.news.storage.mapper;

import com.example.web.news.entity.CategoryEntity;
import com.example.web.news.injections.mapper.MapperCategory;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@RequiredArgsConstructor
@Component
public class RowMapperCategory implements MapperCategory<CategoryEntity> {

    public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CategoryEntity(
                rs.getLong(CategoryEntity.Fields.id),
                rs.getString(CategoryEntity.Fields.title));
    }

    @Override
    public RowMapperCategory createData(){
        return new RowMapperCategory();
    }
}
