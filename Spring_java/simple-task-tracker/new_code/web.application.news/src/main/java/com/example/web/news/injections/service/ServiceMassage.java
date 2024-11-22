package com.example.web.news.injections.service;

import com.example.web.news.entity.MassageEntity;

import java.util.List;

public interface ServiceMassage {
    MassageEntity save(MassageEntity massageEntity);
    boolean delete(Long id);
    List<MassageEntity> getAll();
}
