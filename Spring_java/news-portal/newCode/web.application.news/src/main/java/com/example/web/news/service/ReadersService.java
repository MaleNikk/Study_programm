package com.example.web.news.service;

import com.example.web.news.entity.ReaderEntity;
import com.example.web.news.injections.management.ManagementReaders;
import com.example.web.news.injections.service.ServiceReaders;
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
public class ReadersService implements ServiceReaders<ReaderEntity> {

    private final ManagementReaders<ReaderEntity> managementReaders;

    @Override
    public List<ReaderEntity> searchAll() {
        log.info("Call method searchAll of ReaderService");
        return getManagementReaders().searchAll();
    }

    @Override
    public ReaderEntity searchById(Long id) {
        log.info("Call method searchById of ReaderService");
        return getManagementReaders().searchById(id);
    }

    @Override
    public ReaderEntity save(ReaderEntity readerEntity) {
        log.info("Call method save of ReaderService");
        return getManagementReaders().save(readerEntity);
    }

    @Override
    public ReaderEntity edit(ReaderEntity readerEntity) {
        log.info("Call method edit of ReaderService");
        return getManagementReaders().edit(readerEntity);
    }

    @Override
    public void removeById(Long id) {
        log.info("Call method removeById of ReaderService");
        getManagementReaders().removeById(id);
    }

    @Override
    public void batchInsert(List<ReaderEntity> readerEntities) {
        log.info("Call method batchInsert of ReaderService");
        getManagementReaders().batchInsert(readerEntities);
    }

    @Override
    public HashSet<Long> numbersId() {
        log.info("Call method numbersId of ReaderService");
        return getManagementReaders().numbersId();
    }
}
