package com.example.web.news.security.injection;

import com.example.web.news.security.model.AppSecurityModelEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

@EnableMongoRepositories
@Repository
public interface NewsAppSecurityRepository extends MongoRepository<AppSecurityModelEntity, String> {
}
