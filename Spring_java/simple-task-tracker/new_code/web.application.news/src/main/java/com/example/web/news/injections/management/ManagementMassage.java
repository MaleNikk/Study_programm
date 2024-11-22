package com.example.web.news.injections.management;

import com.example.web.news.entity.MassageEntity;
import java.util.List;

public interface ManagementMassage {
    MassageEntity save(MassageEntity massageEntity);
    boolean delete(Long id);
    List<MassageEntity> getAll();
}
