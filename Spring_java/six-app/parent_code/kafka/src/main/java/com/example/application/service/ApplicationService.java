package com.example.application.service;

import com.example.application.inject.ServiceApplication;
import com.example.application.model.MessageEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService implements ServiceApplication {

    private final List<MessageEntity> messageEntities = new ArrayList<>();

    public void add(MessageEntity message) {
        messageEntities.add(message);
    }

    public Optional<MessageEntity> getById(Long id){
        return messageEntities.stream().filter(km -> km.id().equals(id)).findFirst();
    }
}
