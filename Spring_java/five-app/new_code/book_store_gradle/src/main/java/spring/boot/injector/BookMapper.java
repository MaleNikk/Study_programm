package spring.boot.injector;

import org.springframework.jdbc.core.RowMapper;
import spring.boot.dto.BookEntity;
import spring.boot.mapper.MapperBooks;

public interface BookMapper<ApplicationEntity> extends RowMapper<ApplicationEntity> {
    MapperBooks createData();
    ApplicationEntity castToType(BookEntity book);
    BookEntity castToType(ApplicationEntity applicationEntity);
}
