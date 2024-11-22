package com.example.web.news.service;

import com.example.web.news.entity.NewsEntity;
import com.example.web.news.storage.ContactNews;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Data
@Service
@Slf4j
@RequiredArgsConstructor
public class NewServices implements NewsService {

    private final ContactNews contactNews;

    @Override
    public List<NewsEntity> searchAll() {
        log.debug("Enjoy service method search all");
        return contactNews.searchAll();
    }

    @Override
    public NewsEntity searchById(Long id) {
        log.debug("Enjoy service method search by id: {}", id);
        return contactNews.searchNewsById(id).orElse(null);
    }

    @Override
    public NewsEntity saveNews(NewsEntity newsEntity) {
        log.debug("Enjoy method service save newsEntity: {} ", newsEntity);
        return contactNews.saveNews(newsEntity);
    }

    @Override
    public NewsEntity editNews(NewsEntity newsEntity) {
        log.debug("Enjoy service method edit newsEntity: {}", newsEntity);
        return contactNews.editNews(newsEntity);
    }

    @Override
    public void removeNewstById(Long id) {
        log.debug("Enjoy service method remove contact by id: {}", id);
        contactNews.removeNewsById(id);
    }

    @Override
    public void batchInsert(List<NewsEntity> newEntities) {
        log.debug("Enjoy service method batch insert.");
        contactNews.batchInsert(newEntities);
    }

    @Override
    public HashSet<Long> numbersId(){
        log.debug("Enjoy service method numbersId.");
        return contactNews.numbersId();
    }
}
