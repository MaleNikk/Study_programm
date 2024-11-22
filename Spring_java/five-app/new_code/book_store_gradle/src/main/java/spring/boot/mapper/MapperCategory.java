package spring.boot.mapper;

import org.springframework.stereotype.Component;
import spring.boot.dto.ApplicationEntity;
import spring.boot.dto.CategoryEntity;
import spring.boot.injector.CategoryMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MapperCategory implements CategoryMapper<ApplicationEntity> {
    @Override
    public ApplicationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.getIntegers()[0] = rs.getInt(1);
        applicationEntity.getStrings()[0] = rs.getString(2);
        applicationEntity.getStrings()[1] = rs.getString(3);
        return applicationEntity;
    }

    @Override
    public MapperCategory createData() {
        return new MapperCategory();
    }

    @Override
    public ApplicationEntity castToType(CategoryEntity category) {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.getIntegers()[0] = category.getId();
        applicationEntity.getStrings()[0] = category.getText();
        applicationEntity.getStrings()[1] = category.getDescription();
        return applicationEntity;
    }

    @Override
    public CategoryEntity castToType(ApplicationEntity entity) {
        CategoryEntity category = new CategoryEntity();
        category.setId(entity.getIntegers()[0]);
        category.setText(entity.getStrings()[0]);
        category.setDescription(entity.getStrings()[1]);
        return category;
    }
}
