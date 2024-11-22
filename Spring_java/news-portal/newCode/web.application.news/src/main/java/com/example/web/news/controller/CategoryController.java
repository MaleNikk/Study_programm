package com.example.web.news.controller;

import com.example.web.news.entity.CategoryEntity;
import com.example.web.news.injections.service.ServiceCategories;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;
@Getter
@Controller
@RequestMapping
@RequiredArgsConstructor
public class CategoryController {

    private final ServiceCategories<CategoryEntity> categories;
    @GetMapping("/category")
    public String showAllCategories(Model model){
        model.addAttribute("categories",getCategories().searchAll());
        return "category/category";
    }
    @GetMapping("/category/createCategory")
    public String addCategory(Model model) {
        model.addAttribute("categoryEntity", new CategoryEntity());
        return "category/createCategory";
    }

    @PostMapping("/category/createCategory")
    public String addCategoryComplete(@ModelAttribute CategoryEntity categoryEntity) {
        AtomicLong atomicLong = new AtomicLong(getCategories().searchAll().size());
        long id;
        do { id = atomicLong.getAndIncrement(); } while (getCategories().numbersId().contains(id));
        getCategories().numbersId().add(id);
        categoryEntity.setId(id);
        getCategories().save(categoryEntity);
        return "redirect:/";
    }
    @GetMapping("/category/editCategory/{id}")
    public String editCategories(@PathVariable Long id, Model model) {
        CategoryEntity category = getCategories().searchById(id);
        if (category != null) {
            model.addAttribute("categoryEntity", category);
            return "category/editCategory";
        }
        return "redirect:/";
    }

    @PostMapping("/category/editCategory")
    public String editCategoriesComplete(@ModelAttribute CategoryEntity categoryEntity) {
        getCategories().edit(categoryEntity);
        return "redirect:/";
    }
    @GetMapping("showNews/{id}")
    public String showNewsByCategoryId(@PathVariable Long id,Model model){
        model.addAttribute("newsByCategory",getCategories().newsCategory(id));
        return "news/showNews";
    }
    @GetMapping("/category/deleteCategory/{id}")
    public String deleteCategories(@PathVariable Long id) {
        getCategories().removeById(id);
        getCategories().numbersId().remove(id);
        return "redirect:/";
    }
    @GetMapping("/category/comeback")
    public String comeback() {
        return "redirect:/";
    }

}
