package com.example.application.inject;

import com.example.application.model.MessageEntity;

import java.util.Optional;

public interface ServiceApplication {
    Optional<MessageEntity> getById(Long id);

    void add(MessageEntity message);
}
