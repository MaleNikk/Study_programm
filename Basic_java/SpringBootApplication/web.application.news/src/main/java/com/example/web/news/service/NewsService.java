package com.example.web.news.service;

import com.example.web.news.entity.NewsEntity;

import java.util.HashSet;
import java.util.List;

public interface NewsService {
    List<NewsEntity> searchAll();

    NewsEntity searchById(Long id);

    NewsEntity saveNews(NewsEntity newsEntity);

    NewsEntity editNews(NewsEntity newsEntity);

    void removeNewstById(Long Id);

    void batchInsert(List<NewsEntity> newEntities);

    HashSet<Long> numbersId();
}
