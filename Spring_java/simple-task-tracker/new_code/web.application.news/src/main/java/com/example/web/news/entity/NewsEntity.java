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
public class NewsEntity {
    private long id, category_id;
    private String title, news, author, date_news;
}
