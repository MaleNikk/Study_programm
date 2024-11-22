package com.example.web.news.controller;

import com.example.web.news.entity.CategoryEntity;
import com.example.web.news.entity.CommentEntity;
import com.example.web.news.entity.NewsEntity;
import com.example.web.news.injections.service.ServiceCategories;
import com.example.web.news.injections.service.ServiceComments;
import com.example.web.news.injections.service.ServiceNews;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;
@Getter
@Controller
@RequestMapping
@RequiredArgsConstructor
public class NewsController {

    private final ServiceNews<NewsEntity> serviceNews;
    private final ServiceCategories<CategoryEntity> categories;
    private final ServiceComments<CommentEntity> serviceComments;

    @GetMapping("/")
    public String allNews(Model model) {
        model.addAttribute("news", getServiceNews().searchAll());
        model.addAttribute("categories", getCategories().searchAll());
        return "news/startPage";
    }

    @GetMapping("news/create")
    public String addNews(Model model) {
        model.addAttribute("newsEntity", new NewsEntity());
        return "news/create";
    }

    @PostMapping("news/create")
    public String addNewsComplete(@ModelAttribute NewsEntity newsEntity) {
        AtomicLong atomicLong = new AtomicLong(getServiceNews().searchAll().size());
        long id;
        do { id = atomicLong.getAndIncrement(); } while (getServiceNews().numbersId().contains(id));
        getServiceNews().numbersId().add(id);
        newsEntity.setId(id);
        newsEntity.setDate_news(LocalDateTime.now().toString());
        getServiceNews().save(newsEntity);
        return "redirect:/";
    }

    @GetMapping("news/edit/{id}")
    public String editNews(@PathVariable Long id, Model model) {
        NewsEntity newsEntity = getServiceNews().searchById(id);
        if (newsEntity != null) {
            newsEntity.setDate_news(LocalDateTime.now().toString());
            model.addAttribute("newsEntity", newsEntity);
            return "news/edit";
        }
        return "redirect:/";
    }

    @PostMapping("news/edit")
    public String editNewsComplete(@ModelAttribute NewsEntity newsEntity) {
        getServiceNews().edit(newsEntity);
        return "redirect:/";
    }

    @GetMapping("news/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        NewsEntity news = getServiceNews().searchById(id);
        getServiceNews().removeById(id);
        getServiceNews().numbersId().remove(id);
        getServiceComments().commentsByNewsId(news.getId()).iterator().forEachRemaining(commentEntity -> {
            getServiceComments().numbersId().remove(commentEntity.getId());
            getServiceComments().removeById(commentEntity.getId());
        });
        return "redirect:/";
    }
}
