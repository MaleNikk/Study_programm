package com.example.web.news.management;

import com.example.web.news.entity.NewsEntity;
import com.example.web.news.service.NewsService;
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

    private final NewsService newsService;

    @GetMapping("/")
    public String allNews(Model model) {
        model.addAttribute("news", newsService.searchAll());
        return "startpage.html";
    }

    @GetMapping("/news/create")
    public String addNews(Model model) {
        model.addAttribute("newsEntity", new NewsEntity());
        return "create";
    }

    @PostMapping("/news/create")
    public String addComplete(@ModelAttribute NewsEntity newsEntity) {
        AtomicLong atomicLong = new AtomicLong(newsService.searchAll().size());
        long id;
        do { id = atomicLong.getAndIncrement(); } while (newsService.numbersId().contains(id));
        newsService.numbersId().add(id);
        newsEntity.setId(id);
        newsEntity.setDate_news(LocalDateTime.now().toString());
        newsService.saveNews(newsEntity);
        return "redirect:/";
    }

    @GetMapping("/news/edit/{id}")
    public String editNews(@PathVariable Long id, Model model) {
        NewsEntity newsEntity = newsService.searchById(id);
        if (newsEntity != null) {
            newsEntity.setDate_news(LocalDateTime.now().toString());
            model.addAttribute("newsEntity", newsEntity);
            return "edit";
        }
        return "redirect:/";
    }

    @PostMapping("/news/edit")
    public String editComplete(@ModelAttribute NewsEntity newsEntity) {
        newsService.editNews(newsEntity);
        return "redirect:/";
    }

    @GetMapping("news/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        newsService.removeNewstById(id);
        newsService.numbersId().remove(id);
        return "redirect:/";
    }
}
