package com.example.web.news.controller;

import com.example.web.news.entity.CategoryEntity;
import com.example.web.news.entity.CommentEntity;
import com.example.web.news.entity.NewsEntity;
import com.example.web.news.entity.ReaderEntity;
import com.example.web.news.injections.service.ServiceCategories;
import com.example.web.news.injections.service.ServiceComments;
import com.example.web.news.injections.service.ServiceNews;
import com.example.web.news.injections.service.ServiceReaders;
import com.example.web.news.security.injection.NewsAppSecurityService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;


@Getter
@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class NewsController {

    private final ServiceNews<NewsEntity> serviceNews;

    private final ServiceCategories<CategoryEntity> categories;

    private final ServiceComments<CommentEntity> serviceComments;

    private final NewsAppSecurityService securityService;

    private final ServiceReaders<ReaderEntity> serviceReaders;

    @GetMapping("/")
    public String allNews(Model model) {
        log.info("Init method allNews in class NewsController.");
        model.addAttribute("news", getServiceNews().searchAll());
        model.addAttribute("categories", getCategories().searchAll());
        return "news/startPage";
    }

    @GetMapping("/news/management")
    public String managementConnect(Model model) {
        log.info("Init method managementConnect in class NewsController.");
        ReaderEntity readerEntity = new ReaderEntity();
        model.addAttribute("readerEntity", readerEntity);
        return "news/checkRole";

    }

    @PostMapping("/news/checkRole")
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
                model.addAttribute("news", getServiceNews().searchAll());
                return "news/managementNewsModerator";
            }
            if (securityService.checkRoleAdmin(pin)) {
                model.addAttribute("news", getServiceNews().searchAll());
                return "news/managementNewsAdmin";
            }
        }
        return "security/checkRegistration";
    }

    @GetMapping("news/create")
    public String addNews(Model model) {
        log.info("Init method addNews in class NewsController.");
            model.addAttribute("newsEntity", new NewsEntity());
            return "news/create";
    }

    @PostMapping("news/create")
    public String addNewsComplete(@ModelAttribute NewsEntity newsEntity) {
        log.info("Init method addNewsComplete in class NewsController.");
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
        log.info("Init method editNews in class NewsController.");
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
        log.info("Init method editNewsComplete in class NewsController.");
        getServiceNews().edit(newsEntity);
        return "redirect:/";
    }

    @GetMapping("news/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        log.info("Init method deleteNews in class NewsController.");
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
