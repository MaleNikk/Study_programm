package spring.boot.injector;

import org.springframework.jdbc.core.RowMapper;
import spring.boot.dto.CategoryEntity;
import spring.boot.mapper.MapperCategory;

public interface CategoryMapper<ApplicationEntity> extends RowMapper<ApplicationEntity> {
    MapperCategory createData();
    ApplicationEntity castToType(CategoryEntity category);
    CategoryEntity castToType(ApplicationEntity applicationEntity);
}
