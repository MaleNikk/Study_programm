package com.example.web.news.service;

import com.example.web.news.entity.CategoryEntity;
import com.example.web.news.entity.NewsEntity;
import com.example.web.news.storage.management.ManagementCategories;
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
public class CategoriesService implements ServiceCategories<CategoryEntity> {

    private final ManagementCategories<CategoryEntity> categoriesManagement;
    private final ManagementNews<NewsEntity> managementNews;
    @Override
    public List<CategoryEntity> searchAll() {
        log.info("CategoriesService call method searchAll.");
        return categoriesManagement.searchAll();
    }

    @Override
    public CategoryEntity searchById(Long id) {
        log.info("CategoriesService call method searchById.");
        return categoriesManagement.searchById(id);
    }

    @Override
    public CategoryEntity save(CategoryEntity category) {
        log.info("CategoriesService call method save.");
        return categoriesManagement.save(category);
    }

    @Override
    public CategoryEntity edit(CategoryEntity category) {
        log.info("CategoriesService call method eit.");
        return categoriesManagement.edit(category);
    }

    @Override
    public void removeById(Long id) {
        log.info("CategoriesService call method removeById.");
        categoriesManagement.removeById(id);
    }

    @Override
    public void batchInsert(List<CategoryEntity> categories) {
        log.info("CategoriesService call method batchInsert.");
        categoriesManagement.batchInsert(categories);
    }

    @Override
    public HashSet<Long> numbersId() {
        log.info("CategoriesService call method numbersId.");
      return categoriesManagement.numbersId();
    }

    @Override
    public List<NewsEntity> newsCategory(Long id) {
        log.info("CategoriesService call method nesCategory.");
        return managementNews.searchAll().stream().filter(news->id.equals(news.getCategory_id())).toList();
    }
}
