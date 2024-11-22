package com.example.web.news.service;

import com.example.web.news.entity.CommentEntity;
import com.example.web.news.injections.management.ManagementComments;
import com.example.web.news.injections.service.ServiceComments;
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
public class CommentsService implements ServiceComments<CommentEntity> {

    private final ManagementComments<CommentEntity> managementComments;

    @Override
    public List<CommentEntity> searchAll() {
        log.info("CommentsService call method searchAll.");
        return getManagementComments().searchAll();
    }

    @Override
    public CommentEntity searchById(Long id) {
        log.info("CommentsService call method searchById.");
        return getManagementComments().searchById(id);
    }

    @Override
    public CommentEntity save(CommentEntity comment) {
        log.info("CommentsService call method save.");
        return getManagementComments().save(comment);
    }

    @Override
    public CommentEntity edit(CommentEntity comment) {
        log.info("CommentsService call method edit.");
        return getManagementComments().edit(comment);
    }

    @Override
    public void removeById(Long id) {
        log.info("CommentsService call method removeById.");
        getManagementComments().removeById(id);
    }

    @Override
    public void batchInsert(List<CommentEntity> commentEntities) {
        log.info("CommentsService call method batchInsert.");
        getManagementComments().batchInsert(commentEntities);

    }

    @Override
    public HashSet<Long> numbersId() {
        log.info("CommentsService call method numbersId.");
        return getManagementComments().numbersId();
    }

    @Override
    public List<CommentEntity> commentsByNewsId(Long id) {
        log.info("CommentsService call method commentsByNewsId.");
        return getManagementComments().commentsByNewsId(id);
    }

    @Override
    public List<CommentEntity> commentsByAuthorName(String author) {
        log.info("CommentsService call method commentsByAuthorName.");
        return getManagementComments().commentsByAuthorName(author);

    }

    @Override
    public boolean isPresentPinReader(Long pin) {
        log.info("CommentsService call method isPresentPinReader.");
        return getManagementComments().isPresentPinReader(pin);
    }
}
