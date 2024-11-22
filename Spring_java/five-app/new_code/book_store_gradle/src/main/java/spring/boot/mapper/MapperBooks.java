package spring.boot.mapper;

import org.springframework.stereotype.Component;
import spring.boot.dto.ApplicationEntity;
import spring.boot.dto.BookEntity;
import spring.boot.injector.BookMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MapperBooks implements BookMapper<ApplicationEntity> {

    @Override
    public ApplicationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.getIntegers()[0] = rs.getInt(1);
        applicationEntity.getIntegers()[1] = rs.getInt(2);
        applicationEntity.getIntegers()[2] = rs.getInt(3);
        applicationEntity.getStrings()[0] = rs.getString(4);
        applicationEntity.getStrings()[1] = rs.getString(5);
        applicationEntity.getStrings()[2] = rs.getString(6);
        return applicationEntity;
    }

    @Override
    public MapperBooks createData() {
        return new MapperBooks();
    }

    @Override
    public ApplicationEntity castToType(BookEntity book) {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.getIntegers()[0] = book.getId();
        applicationEntity.getIntegers()[1] = book.getPrice();
        applicationEntity.getIntegers()[2] = book.getCategory_id();
        applicationEntity.getStrings()[0] = book.getAuthor();
        applicationEntity.getStrings()[1] = book.getTitle();
        applicationEntity.getStrings()[2] = book.getDate_print();
        return applicationEntity;
    }

    @Override
    public BookEntity castToType(ApplicationEntity entity) {
        BookEntity book = new BookEntity();
        book.setId(entity.getIntegers()[0]);
        book.setPrice(entity.getIntegers()[1]);
        book.setCategory_id(entity.getIntegers()[2]);
        book.setAuthor(entity.getStrings()[0]);
        book.setTitle(entity.getStrings()[1]);
        book.setDate_print(entity.getStrings()[2]);
        return book;
    }
}
