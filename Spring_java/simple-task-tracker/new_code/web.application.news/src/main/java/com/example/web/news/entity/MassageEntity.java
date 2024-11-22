package com.example.web.news.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Component
public class MassageEntity {
    private Long id;
    private String author;
    private String title;
    private String date_resent;
}
