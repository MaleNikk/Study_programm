package com.example.web.news.listener;

import com.example.web.news.entity.CategoryEntity;
import com.example.web.news.entity.NewsEntity;
import com.example.web.news.service.ServiceCategories;
import com.example.web.news.service.ServiceNews;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class DataBaseListener {

    private final ServiceNews<NewsEntity> newsManagement;

    private final ServiceCategories<CategoryEntity> categoriesManagement;

    @EventListener(ApplicationStartedEvent.class)
    public void generateNews() {
        log.info("Generate default newEntities.");
        List<NewsEntity> newEntities = new ArrayList<>();

        if (newsManagement.searchAll().isEmpty()) {
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
                            Controller — эта аннотация делает класс «контроллером» — классом, методы которого
                                          отвечают за обработку HTTP-запросов и формирование ответов.
                            """
                    , "NameAuthor.SurNameAuthor", LocalDateTime.now().toString()));
            newEntities.add(new NewsEntity(5L, 3L, "New's № 5",
                    """
                            @Autowired — это аннотация в Spring Framework,
                                         используемая для автоматической инъекции (внедрения) зависимостей.
                                         Она позволяет Spring автоматически находить подходящий бин (компонент) и
                                         внедрять его в поле, метод или конструктор класса без необходимости явно
                                         создавать экземпляр зависимости.
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
            newsManagement.batchInsert(newEntities);
        }
    }

    @EventListener(ApplicationStartedEvent.class)
    public void generateCategories(){
        log.info("Generate default newCategories.");
        List<CategoryEntity> newCategories = new ArrayList<>();
        if (categoriesManagement.searchAll().isEmpty()){
            newCategories.add(new CategoryEntity(1L,"Science."));
            newCategories.add(new CategoryEntity(2L,"Programing."));
            newCategories.add(new CategoryEntity(3L,"Wild world."));
            newCategories.add(new CategoryEntity(4L,"Fantasy."));
            categoriesManagement.batchInsert(newCategories);
        }
    }
}
