package com.example.web.news.service;

import com.example.web.news.entity.NewsEntity;
import com.example.web.news.storage.management.ManagementNews;
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
public class NewsServices implements ServiceNews<NewsEntity> {

    private final ManagementNews<NewsEntity> newsManagement;

    @Override
    public List<NewsEntity> searchAll() {
        log.info("Enjoy service method search all");
        return newsManagement.searchAll();
    }

    @Override
    public NewsEntity searchById(Long id) {
        log.info("Enjoy service method search by id: {}", id);
        return newsManagement.searchById(id);
    }

    @Override
    public NewsEntity save(NewsEntity newsEntity) {
        log.info("Enjoy method service save newsEntity: {} ", newsEntity);
        return newsManagement.save(newsEntity);
    }

    @Override
    public NewsEntity edit(NewsEntity newsEntity) {
        log.info("Enjoy service method edit newsEntity: {}", newsEntity);
        return newsManagement.edit(newsEntity);
    }

    @Override
    public void removeById(Long id) {
        log.info("Enjoy service method remove contact by id: {}", id);
        newsManagement.removeById(id);
    }

    @Override
    public void batchInsert(List<NewsEntity> newEntities) {
        log.info("Enjoy service method batch insert.");
        newsManagement.batchInsert(newEntities);
    }

    @Override
    public HashSet<Long> numbersId(){
        log.info("Enjoy service method numbersId.");
        return newsManagement.numbersId();
    }
}
