package com.example.web.news.storage.management;

import com.example.web.news.entity.NewsEntity;
import com.example.web.news.service.ServiceNews;

import java.util.List;

public interface ManagementCategories<CategoryEntity> extends ServiceNews<CategoryEntity> {
    List<NewsEntity> newsCategory(Long id);
}
