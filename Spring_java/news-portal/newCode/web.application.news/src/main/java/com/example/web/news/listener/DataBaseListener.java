package com.example.web.news.listener;

import com.example.web.news.entity.CategoryEntity;
import com.example.web.news.entity.CommentEntity;
import com.example.web.news.entity.NewsEntity;
import com.example.web.news.entity.ReaderEntity;
import com.example.web.news.injections.service.ServiceCategories;
import com.example.web.news.injections.service.ServiceComments;
import com.example.web.news.injections.service.ServiceNews;
import com.example.web.news.injections.service.ServiceReaders;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Component
@RequiredArgsConstructor
@Slf4j
public class DataBaseListener {

    private final ServiceNews<NewsEntity> newsManagement;

    private final ServiceCategories<CategoryEntity> categoriesManagement;

    private final ServiceReaders<ReaderEntity> serviceReaders;

    private final ServiceComments<CommentEntity> serviceComments;

    @EventListener(ApplicationStartedEvent.class)
    public void generateNews() {
        log.info("Generate default newEntities.");
        List<NewsEntity> newEntities = new ArrayList<>();

        if (getNewsManagement().searchAll().isEmpty()) {
            newEntities.add(new NewsEntity(1L, 2L,"New's № 1",
                    "Model - Работа с базой данных","NameAuthor.SurNameAuthor",
                    LocalDateTime.now().toString()));
            newEntities.add(new NewsEntity(2L, 2L, "New's № 2",
                    "View - Отображение данных пользователям","NameAuthor.SurNameAuthor",
                    LocalDateTime.now().toString()));
            newEntities.add(new NewsEntity(3L, 1L, "New's № 3",
                    "Controller - Обработка запросов и формирование ответов", "NameAuthor.SurNameAuthor",
                    LocalDateTime.now().toString()));
            newEntities.add(new NewsEntity(4L, 1L, "New's № 4",
                    """
                            Controller — эта аннотация делает класс «контроллером» (методы обрабатывают HTTP-запросы и
                             формируются ответы).
                            """
                    , "NameAuthor.SurNameAuthor", LocalDateTime.now().toString()));
            newEntities.add(new NewsEntity(5L, 3L, "New's № 5",
                    """
                            @Autowired — это аннотация в Spring Framework,
                                         используемая для автоматической инъекции (внедрения) зависимостей.
                                         Она позволяет Spring автоматически находить подходящий бин (компонент) и
                                         внедрять его в поле.
                            """, "NameAuthor.SurNameAuthor", LocalDateTime.now().toString()));
            newEntities.add(new NewsEntity(6L, 4L, "New's № 6",
                    """
                            @Value позволяет внедрять значения из application.properties непосредственно в код.
                                   Вы можете использовать её над полями, методами или даже параметрами методов.
                            """, "NameAuthor.SurNameAuthor", LocalDateTime.now().toString()));
            newEntities.add(new NewsEntity(7L, 4L, "New's № 7",
                    """
                            @ConfigurationProperties позволяет связать значения из файлов application.properties
                                                     с полями класса в вашем приложении.
                            """, "NameAuthor.SurNameAuthor", LocalDateTime.now().toString()));
            getNewsManagement().batchInsert(newEntities);
        }
    }

    @EventListener(ApplicationStartedEvent.class)
    public void generateCategories(){
        log.info("Generate default newCategories.");
        List<CategoryEntity> newCategories = new ArrayList<>();
        if (getCategoriesManagement().searchAll().isEmpty()){
            newCategories.add(new CategoryEntity(1L,"Science."));
            newCategories.add(new CategoryEntity(2L,"Programing."));
            newCategories.add(new CategoryEntity(3L,"Wild world."));
            newCategories.add(new CategoryEntity(4L,"Fantasy."));
            getCategoriesManagement().batchInsert(newCategories);
        }
    }

    @EventListener(ApplicationStartedEvent.class)
    public void generateReaders(){
        log.info("Generate default readers.");
        List<ReaderEntity> newReaders = new ArrayList<>();
        if (getServiceReaders().searchAll().isEmpty()){
            newReaders.add(new ReaderEntity(1L,12345L,"Name_1","Surname_1"));
            newReaders.add(new ReaderEntity(2L,23456L,"Name_2","Surname_2"));
            newReaders.add(new ReaderEntity(3L,34567L,"Name_3","Surname_3"));
            newReaders.add(new ReaderEntity(4L,45678L,"Name_4","Surname_4"));
            newReaders.add(new ReaderEntity(5L,56789L,"Name_5","Surname_5"));
            getServiceReaders().batchInsert(newReaders);
        }
    }

    @EventListener(ApplicationStartedEvent.class)
    public void generateComments(){
        log.info("Generate default comments.");
        List<CommentEntity> commentEntityList = new ArrayList<>();
        long[] pinReaders = {12345L, 23456L, 34567L, 45678L, 56789L};
        String authorName = "Name";
        String authorSurname = "Surname";
        if (getServiceComments().searchAll().isEmpty()){
            AtomicLong atomicLong = new AtomicLong(1);
            for (int i = 1; i <= 7; i++){
                for (int c = 1; c <= 5; c++){
                    StringBuilder builder = new StringBuilder();
                    CommentEntity comment = new CommentEntity();
                    comment.setId(atomicLong.getAndIncrement());
                    comment.setId_news(i);
                    comment.setPin_reader(pinReaders[c-1]);
                    comment.setAuthor(builder.
                            append(authorName).
                            append("_").
                            append(c).
                            append(" ").
                            append(authorSurname).
                            append("_").
                            append(c).toString());
                    comment.setText("Conversation about the meaning of news and involvement in life situations.");
                    comment.setDate_comment(LocalDateTime.now().toString());
                    commentEntityList.add(comment);
                }
            }
        }
        getServiceComments().batchInsert(commentEntityList);
    }
}
