package spring.boot.controllertest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import spring.boot.dto.ApplicationEntity;
import spring.boot.dto.BookEntity;
import spring.boot.dto.CategoryEntity;

@SpringBootTest(classes = TestWebController.class)
@AutoConfigureMockMvc
public abstract class AbstractTestController {

    @Autowired
    protected MockMvc mockMvc;


    protected BookEntity createBook(Integer id, CategoryEntity category) {
        BookEntity book = new BookEntity();
        book.setId(id);
        book.setPrice(2500);
        book.setCategory_id(category.getId());
        book.setAuthor("Test author");
        book.setTitle("Check book title.");
        book.setDate_print("2017.07.07T00.00.00");
        return book;
    }

    protected CategoryEntity createCategory(Integer id) {
        CategoryEntity category = new CategoryEntity();
        category.setId(id);
        category.setText("New category.");
        category.setDescription("Category about new.");
        return category;
    }
}
