package spring.boot.controllertest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import spring.boot.dto.ApplicationEntity;
import spring.boot.dto.BookEntity;
import spring.boot.dto.CategoryEntity;
import spring.boot.injector.BookMapper;
import spring.boot.injector.CategoryMapper;
import spring.boot.injector.ServiceManagement;

import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc
public class TestWebController extends AbstractTestController {

    @MockBean
    private ServiceManagement<ApplicationEntity> serviceManagement;

    @MockBean
    private BookMapper<ApplicationEntity> bookMapper;

    @MockBean
    private CategoryMapper<ApplicationEntity> categoryMapper;


    @Test
    public void whenGetAll_ThenReturnAllBooks() throws Exception {
        List<BookEntity> books = new ArrayList<>();
        books.add(createBook(1, createCategory(1)));
        List<BookEntity> booksFromDB = new ArrayList<>();
        for (ApplicationEntity entity : serviceManagement.viewAll("books")) {
            booksFromDB.add(bookMapper.castToType(entity));
        }

        Mockito.when(booksFromDB).thenReturn(books);

    }

    @Test
    public void whenGetAll_ThenReturnAllCategories() throws Exception {
        List<CategoryEntity> categoriesVerify = new ArrayList<>();
        categoriesVerify.add(createCategory(1));
        List<CategoryEntity> categoriesFromDB = new ArrayList<>();
        for (ApplicationEntity entity : serviceManagement.viewAll("category")) {
            categoriesFromDB.add(categoryMapper.castToType(entity));
        }

        Mockito.when(categoriesFromDB).thenReturn(categoriesVerify);

    }
}
