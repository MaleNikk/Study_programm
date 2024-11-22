package com.example.web.news.controller;

import com.example.web.news.entity.CategoryEntity;
import com.example.web.news.entity.NewsEntity;
import com.example.web.news.service.ServiceCategories;
import com.example.web.news.service.ServiceNews;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class WebController {

    private final ServiceNews<NewsEntity> serviceNews;
    private final ServiceCategories<CategoryEntity> categories;

    @GetMapping("/")
    public String allNews(Model model) {
        model.addAttribute("news", serviceNews.searchAll());
        model.addAttribute("categories", categories.searchAll());
        return "startPage";
    }

    @GetMapping("/news/create")
    public String addNews(Model model) {
        model.addAttribute("newsEntity", new NewsEntity());
        return "create";
    }

    @PostMapping("/news/create")
    public String addNewsComplete(@ModelAttribute NewsEntity newsEntity) {
        AtomicLong atomicLong = new AtomicLong(serviceNews.searchAll().size());
        long id;
        do { id = atomicLong.getAndIncrement(); } while (serviceNews.numbersId().contains(id));
        serviceNews.numbersId().add(id);
        newsEntity.setId(id);
        newsEntity.setDate_news(LocalDateTime.now().toString());
        serviceNews.save(newsEntity);
        return "redirect:/";
    }

    @GetMapping("/news/edit/{id}")
    public String editNews(@PathVariable Long id, Model model) {
        NewsEntity newsEntity = serviceNews.searchById(id);
        if (newsEntity != null) {
            newsEntity.setDate_news(LocalDateTime.now().toString());
            model.addAttribute("newsEntity", newsEntity);
            return "edit";
        }
        return "redirect:/";
    }

    @PostMapping("/news/edit")
    public String editNewsComplete(@ModelAttribute NewsEntity newsEntity) {
        serviceNews.edit(newsEntity);
        return "redirect:/";
    }

    @GetMapping("news/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        serviceNews.removeById(id);
        serviceNews.numbersId().remove(id);
        return "redirect:/";
    }
    @GetMapping("/category")
    public String showAllCategories(Model model){
        model.addAttribute("categories",categories.searchAll());
        return "category";
    }
    @GetMapping("/category/createCategory")
    public String addCategory(Model model) {
        model.addAttribute("categoryEntity", new CategoryEntity());
        return "createCategory";
    }

    @PostMapping("/category/createCategory")
    public String addCategoryComplete(@ModelAttribute CategoryEntity categoryEntity) {
        AtomicLong atomicLong = new AtomicLong(categories.searchAll().size());
        long id;
        do { id = atomicLong.getAndIncrement(); } while (categories.numbersId().contains(id));
        categories.numbersId().add(id);
        categoryEntity.setId(id);
        categories.save(categoryEntity);
        return "redirect:/category";
    }
    @GetMapping("/category/editCategory/{id}")
    public String editCategories(@PathVariable Long id, Model model) {
        CategoryEntity category = categories.searchById(id);
        if (category != null) {
            model.addAttribute("categoryEntity", category);
            return "/editCategory";
        }
        return "redirect:/category";
    }

    @PostMapping("/category/editCategory")
    public String editCategoriesComplete(@ModelAttribute CategoryEntity categoryEntity) {
        categories.edit(categoryEntity);
        return "redirect:/category";
    }
    @GetMapping("showNews/{id}")
    public String showNewsByCategoryId(@PathVariable Long id,Model model){
        model.addAttribute("newsByCategory",categories.newsCategory(id));
        return "/showNews";
    }
    @GetMapping("/category/deleteCategory/{id}")
    public String deleteCategories(@PathVariable Long id) {
        categories.removeById(id);
        categories.numbersId().remove(id);
        return "redirect:/category";
    }
    @GetMapping("/category/comeback")
    public String comeback() {
        return "redirect:/";
    }

}
