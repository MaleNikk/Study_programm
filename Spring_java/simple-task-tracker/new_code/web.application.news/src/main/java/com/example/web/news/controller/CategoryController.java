package com.example.web.news.controller;

import com.example.web.news.entity.CategoryEntity;
import com.example.web.news.entity.ReaderEntity;
import com.example.web.news.injections.service.ServiceCategories;
import com.example.web.news.injections.service.ServiceReaders;
import com.example.web.news.security.injection.NewsAppSecurityService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;
@Getter
@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final ServiceCategories<CategoryEntity> categories;

    private final NewsAppSecurityService securityService;

    private final ServiceReaders<ReaderEntity> serviceReaders;

    @GetMapping("/category/management")
    public String managementConnect(Model model) {
        log.info("Init method managementConnect in class NewsController.");
        ReaderEntity readerEntity = new ReaderEntity();
        model.addAttribute("readerEntity", readerEntity);
        return "category/checkRole";
    }

    @PostMapping("/category/checkRole")
    public String managementContinueConnect(@ModelAttribute ReaderEntity readerEntity, Model model) {
        log.info("Init method managementContinueConnect in class NewsController.");
        ReaderEntity checkEntity = serviceReaders.searchAll()
                .stream()
                .filter(reader -> (readerEntity.getName().equals(reader.getName())
                        && readerEntity.getSurname().equals(reader.getSurname())
                        && readerEntity.getPin().equals(reader.getPin())))
                .findFirst().orElse(null);
        if (checkEntity != null) {
            Long pin = checkEntity.getPin();
            if (securityService.checkRoleUser(pin)) {
                return "security/onlyAdminOrModerator";
            }
            if (securityService.checkRoleModerator(pin)) {
                model.addAttribute("categories", getCategories().searchAll());
                return "category/managementCategoryModerator";
            }
            if (securityService.checkRoleAdmin(pin)) {
                model.addAttribute("categories", getCategories().searchAll());
                return "category/managementCategoryAdmin";
            }
        }
        return "security/checkRegistration";
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

}
