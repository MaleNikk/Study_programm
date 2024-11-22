package spring.boot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.boot.dto.ApplicationEntity;
import spring.boot.dto.BookEntity;
import spring.boot.dto.CategoryEntity;
import spring.boot.injector.BookMapper;
import spring.boot.injector.CategoryMapper;
import spring.boot.injector.ServiceManagement;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("rest/bookstore")
public class ApplicationRestController {

    @Autowired
    private ServiceManagement<ApplicationEntity> serviceManagement;

    @Autowired
    private BookMapper<ApplicationEntity> bookMapper;

    @Autowired
    private CategoryMapper<ApplicationEntity> categoryMapper;

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public ResponseEntity<Object> vewAllBooks() {
        List<BookEntity> books = new ArrayList<>();
        for (ApplicationEntity book : serviceManagement.viewAll("books")){
            books.add(bookMapper.castToType(book));
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseEntity<Object> vieAllCategories() {
        List<CategoryEntity> categories = new ArrayList<>();
        for (ApplicationEntity category : serviceManagement.viewAll("category")) {
            categories.add(categoryMapper.castToType(category));
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @RequestMapping(value = "/books/category/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> vewBooksByCategory(@PathVariable("id") Integer id) {
        List<BookEntity> books = new ArrayList<>();
        for (ApplicationEntity entity : serviceManagement.viewAllByCategory("books", id)) {
            books.add(bookMapper.castToType(entity));
        }
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @RequestMapping(value = "/books/author/{author}", method = RequestMethod.GET)
    public ResponseEntity<Object> vewBooksByAuthor(@PathVariable("author") String author) {
        List<BookEntity> books = new ArrayList<>();
        for(ApplicationEntity entity : serviceManagement.viewAllByAuthor("books", author)) {
            books.add(bookMapper.castToType(entity));
        }
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @RequestMapping(value = "/book/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createBook(@RequestBody BookEntity book) {
        serviceManagement.save(bookMapper.castToType(book), "books");
        return new ResponseEntity<>("Book added successfully!",HttpStatus.CREATED);
    }


    @RequestMapping(value = "/category/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createCategory(@RequestBody CategoryEntity category) {
        serviceManagement.save(categoryMapper.castToType(category), "category");
        return new ResponseEntity<>("Category added successfully!",HttpStatus.CREATED);
    }

    @RequestMapping(value = "/books/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteBook(@PathVariable("id") Integer id) {
        serviceManagement.remove(id,"books");
        return new ResponseEntity<>("Book delete successfully!",HttpStatus.OK);
    }

    @RequestMapping(value = "/category/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteCategory(@PathVariable("id") Integer id) {
        serviceManagement.remove(id,"category");
        return new ResponseEntity<>("Category delete successfully!",HttpStatus.OK);
    }

    @RequestMapping(value = "/books/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateBook(@PathVariable("id") Integer id, @RequestBody BookEntity book) {
        serviceManagement.update(id, "books", bookMapper.castToType(book));
        return new ResponseEntity<>("Book updated successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/category/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateCategory(@PathVariable("id") Integer id, @RequestBody CategoryEntity category) {
        serviceManagement.update(id, "category", categoryMapper.castToType(category));
        return new ResponseEntity<>("Category updated successfully", HttpStatus.OK);
    }
}
