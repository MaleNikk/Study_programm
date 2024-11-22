package com.example.application.controller;

import com.example.application.inject.ServiceApplication;
import com.example.application.model.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/web/kafka")
@RequiredArgsConstructor
public class ApplicationController {

    @Value("${spring.kafka.kafkaMessageTopic}")
    private String topicName;

    @Autowired
    private final KafkaTemplate<String, MessageEntity> kafkaTemplate;

    @Autowired
    private final ServiceApplication serviceApplication;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageEntity message) {

        kafkaTemplate.send(topicName, message);

        return ResponseEntity.ok("Message send to kafka.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageEntity> getById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceApplication.getById(id).orElseThrow());
    }
}
