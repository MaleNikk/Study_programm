package com.example.web.news.service;


import java.util.HashSet;
import java.util.List;

public interface ServiceNews<Object> {
    List<Object> searchAll();

    Object searchById(Long id);

    Object save(Object object);

    Object edit(Object object);

    void removeById(Long id);

    void batchInsert(List<Object> newEntities);

    HashSet<Long> numbersId();
}
