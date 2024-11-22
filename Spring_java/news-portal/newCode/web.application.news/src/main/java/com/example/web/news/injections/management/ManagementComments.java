package com.example.web.news.injections.management;

import com.example.web.news.injections.service.ServiceNews;

import java.util.List;

public interface ManagementComments<CommentEntity> extends ServiceNews<CommentEntity> {

    List<CommentEntity> commentsByNewsId(Long id);
    List<CommentEntity> commentsByAuthorName(String author);
    boolean isPresentPinReader(Long pin);
}
