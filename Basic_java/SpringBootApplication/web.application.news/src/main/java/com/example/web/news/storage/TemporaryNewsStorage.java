package com.example.web.news.storage;

import com.example.web.news.entity.NewsEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
@Slf4j
@Data
@NoArgsConstructor
public class TemporaryNewsStorage implements ContactNews {

    private final List<NewsEntity> newEntities = new ArrayList<>();

    private final HashSet<Long> numbersId = new HashSet<>();

    @Override
    public List<NewsEntity> searchAll() {
        log.debug("Enjoy TemporaryNewsStorage method search all.");
        return getNewEntities();
    }

    @Override
    public Optional<NewsEntity> searchNewsById(Long id) {
        log.debug("Enjoy TemporaryNewsStorage method search by Id: {}", id);
        return getNewEntities().stream().filter(contact -> id.equals(contact.getId())).findFirst();
    }

    @Override
    public NewsEntity saveNews(NewsEntity newsEntity) {
        log.debug("Enjoy TemporaryNewsStorage method save newsEntity: {}", newsEntity);
        getNewEntities().add(newsEntity);
        getNumbersId().add(newsEntity.getId());
        return newsEntity;
    }

    @Override
    public NewsEntity editNews(NewsEntity newsEntity) {
        log.debug("Enjoy TemporaryNewsStorage method edit newsEntity: {}", newsEntity);
        NewsEntity editedNewsEntity = searchNewsById(newsEntity.getId()).orElse(null);
        if (editedNewsEntity != null) {
            editedNewsEntity.setId(newsEntity.getId());
            editedNewsEntity.setTitle(newsEntity.getTitle());
            editedNewsEntity.setNews(newsEntity.getNews());
            editedNewsEntity.setAuthor(newsEntity.getAuthor());
            editedNewsEntity.setDate_news(LocalDateTime.now().toString());
        }
        return editedNewsEntity;
    }

    @Override
    public void removeNewsById(Long id) {
        log.debug("Enjoy TemporaryNewsStorage method remove contact by Id: {}", id);
        searchNewsById(id).ifPresent(getNewEntities()::remove);
        if (searchNewsById(id).isPresent()) {
            getNumbersId().remove(id);
        }
    }

    @Override
    public void batchInsert(List<NewsEntity> newEntities) {
        log.debug("Enjoy TemporaryNewsStorage method batchInsert.");
        getNewEntities().addAll(newEntities);
        newEntities.forEach(id -> getNumbersId().add(id.getId()));
    }

    @Override
    public HashSet<Long> numbersId() {
        for (NewsEntity newsEntity : getNewEntities()){
            getNumbersId().add(newsEntity.getId());
        }
        return getNumbersId();
    }
}
