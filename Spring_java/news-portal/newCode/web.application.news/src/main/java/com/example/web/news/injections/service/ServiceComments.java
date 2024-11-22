package com.example.web.news.injections.service;

import java.util.List;

public interface ServiceComments<CommentEntity> extends ServiceNews<CommentEntity> {
    List<CommentEntity> commentsByNewsId(Long id);
    List<CommentEntity> commentsByAuthorName(String author);
    boolean isPresentPinReader(Long pin);
}
