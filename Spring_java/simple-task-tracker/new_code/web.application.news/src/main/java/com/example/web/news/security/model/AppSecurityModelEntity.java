package com.example.web.news.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "management")
public class AppSecurityModelEntity {

    @Id
    private String id;

    private Long user_id;

    private Date date;

    private NewsAppRoles role;
}
