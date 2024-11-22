package com.example.web.news.storage;

import com.example.web.news.entity.NewsEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface ContactNews {

    List<NewsEntity> searchAll();

    Optional<NewsEntity> searchNewsById(Long id);

    NewsEntity saveNews(NewsEntity newsEntity);

    NewsEntity editNews(NewsEntity newsEntity);

    void removeNewsById(Long id);

    void batchInsert(List<NewsEntity> newEntities);

    HashSet<Long> numbersId();

}
