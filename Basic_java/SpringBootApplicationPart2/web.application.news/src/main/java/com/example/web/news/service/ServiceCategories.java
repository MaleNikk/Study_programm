package com.example.web.news.service;

import com.example.web.news.entity.NewsEntity;

import java.util.List;

public interface ServiceCategories<CategoryEntity> extends ServiceNews<CategoryEntity>{
    List<NewsEntity> newsCategory(Long id);
}
