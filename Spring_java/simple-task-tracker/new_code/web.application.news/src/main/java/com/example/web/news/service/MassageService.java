package com.example.web.news.service;

import com.example.web.news.entity.MassageEntity;
import com.example.web.news.injections.management.ManagementMassage;
import com.example.web.news.injections.service.ServiceMassage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
@Slf4j
@RequiredArgsConstructor
public class MassageService implements ServiceMassage {

    private final ManagementMassage managementMassage;

    @Override
    public MassageEntity save(MassageEntity massageEntity) {
        log.info("Init service method save in MassageService class.");
        return managementMassage.save(massageEntity);
    }

    @Override
    public boolean delete(Long id) {
        log.info("Init service method delete in MassageService class.");
        return managementMassage.delete(id);
    }

    @Override
    public List<MassageEntity> getAll() {
        log.info("Init service method getAll in MassageService class.");
        return managementMassage.getAll();
    }
}
