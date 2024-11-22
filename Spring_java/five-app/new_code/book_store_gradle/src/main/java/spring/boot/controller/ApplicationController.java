package spring.boot.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.boot.dto.ApplicationEntity;
import spring.boot.dto.BookEntity;
import spring.boot.dto.CategoryEntity;
import spring.boot.injector.BookMapper;
import spring.boot.injector.CategoryMapper;
import spring.boot.injector.ServiceManagement;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/bookstore")
@RequiredArgsConstructor
public class ApplicationController {

    private final ServiceManagement<ApplicationEntity> serviceManagement;

    private final BookMapper<ApplicationEntity> bookMapper;

    private final CategoryMapper<ApplicationEntity> categoryMapper;



    @GetMapping("/start")
    public String loadStartPage(Model model) {
        List<ApplicationEntity> bookData = serviceManagement.viewAll("books");
        List<ApplicationEntity> categories = serviceManagement.viewAll("category");
        model.addAttribute("allBooks", bookData);
        model.addAttribute("categories", categories);
        log.info("Load start page at time: {}", System.currentTimeMillis());
        return "book/startPage";
    }

    @GetMapping("/book/author/{author}")
    public String showBooksByAuthor(@PathVariable("author") String author, Model model) {
       model.addAttribute("booksByAuthor", serviceManagement.viewAllByAuthor("books", author));
        log.info("Load books by author at time: {}", System.currentTimeMillis());
        return "book/showByAuthor";
    }

    @GetMapping("/book/category{id}")
    public String showBooksByCategory(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("booksByCategory", serviceManagement.viewAllByCategory("books", id));
        log.info("Load books by category at time: {}", System.currentTimeMillis());
        return "book/showByCategory";
    }

    @GetMapping("/book/create")
    public String initialCreateBook(Model model) {
        model.addAttribute("book", new BookEntity());
        log.info("Initial void create book on start page at time: {}", System.currentTimeMillis());
        return "book/create";
    }

    @PostMapping("/book/create")
    public String completeCreateBook(@ModelAttribute BookEntity book) {
        serviceManagement.save(bookMapper.castToType(book), "books");
        log.info("Book was created on start page at time: {}", System.currentTimeMillis());
        return "redirect:/bookstore/start";
    }

    @GetMapping("/book/edit/{id}")
    public String initialEditeBook(@PathVariable Integer id, Model model) {
        model.addAttribute("book",
                bookMapper.castToType(serviceManagement.getById(id,"books")));
        log.info("Initial void edite book on start page at time: {}", System.currentTimeMillis());
        return "book/edit";
    }

    @PostMapping("/book/edit")
    public String completeEditeBook(@ModelAttribute BookEntity book) {
        serviceManagement.update(book.getId(), "books", bookMapper.castToType(book));
        log.info("Book was edited on start page at time: {}", System.currentTimeMillis());
        return "redirect:/bookstore/start";
    }

    @GetMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable Integer id) {
       serviceManagement.remove(id,"books");
       log.info("Initial delete book from database at time: {}", System.currentTimeMillis());
        return "redirect:/bookstore/start";
    }
    @GetMapping("/comeback")
    public String comeback(){
        return "redirect:/bookstore/start";
    }

    @GetMapping("/category/create")
    public String initialCreateCategory(Model model) {
        model.addAttribute("category", new CategoryEntity());
        log.info("Initial void create category at time: {}", System.currentTimeMillis());
        return "category/createCategory";
    }

    @PostMapping("/category/create")
    public String completeCreateCategory(@ModelAttribute CategoryEntity category) {
        serviceManagement.save(categoryMapper.castToType(category), "category");
        log.info("Category was created at time: {}", System.currentTimeMillis());
        return "redirect:/bookstore/start";
    }

    @GetMapping("/category/edit/{id}")
    public String initialEditeCategory(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("category",
                categoryMapper.castToType(serviceManagement.getById(id,"category")));
        log.info("Initial void edite category at time: {}", System.currentTimeMillis());
        return "category/editCategory";
    }

    @PostMapping("/category/edit")
    public String completeEditeCategory(@ModelAttribute CategoryEntity category) {
        serviceManagement.update(category.getId(), "category", categoryMapper.castToType(category));
        log.info("Category was edited at time: {}", System.currentTimeMillis());
        return "redirect:/bookstore/start";
    }

    @GetMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable Integer id) {
        serviceManagement.remove(id,"category");
        log.info("Initial delete category from database at time: {}", System.currentTimeMillis());
        return "redirect:/bookstore/start";
    }
}
