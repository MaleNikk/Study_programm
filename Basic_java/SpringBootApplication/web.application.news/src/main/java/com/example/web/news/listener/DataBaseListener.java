package com.example.web.news.listener;

import com.example.web.news.entity.NewsEntity;
import com.example.web.news.service.NewsService;
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

    private final NewsService newsService;

    @EventListener(ApplicationStartedEvent.class)
    public void generateContacts() {
        log.info("Generate default newEntities.");

        List<NewsEntity> newEntities = new ArrayList<>();
        if (newsService.searchAll().isEmpty()) {
            newEntities.add(new NewsEntity(1, "New's № 1",
                    "Model - Работа с базой данных", "NameAuthor.SurNameAuthor", LocalDateTime.now().toString()));
            newEntities.add(new NewsEntity(2, "New's № 2",
                    "View - Отображение данных пользователям", "NameAuthor.SurNameAuthor", LocalDateTime.now().toString()));
            newEntities.add(new NewsEntity(3, "New's № 3",
                    "Controller - Обработка запросов и формирование ответов", "NameAuthor.SurNameAuthor",
                    LocalDateTime.now().toString()));
            newEntities.add(new NewsEntity(4, "New's № 4",
                    """
                            Controller — эта аннотация делает класс «контроллером» — классом, методы которого
                                          отвечают за обработку HTTP-запросов и формирование ответов.
                            """
                    , "NameAuthor.SurNameAuthor", LocalDateTime.now().toString()));
            newEntities.add(new NewsEntity(5, "New's № 5",
                    """
                            @Autowired — это аннотация в Spring Framework,
                                         используемая для автоматической инъекции (внедрения) зависимостей.
                                         Она позволяет Spring автоматически находить подходящий бин (компонент) и
                                         внедрять его в поле, метод или конструктор класса без необходимости явно
                                         создавать экземпляр зависимости.
                            """, "NameAuthor.SurNameAuthor", LocalDateTime.now().toString()));
            newEntities.add(new NewsEntity(6, "New's № 6",
                    """
                            @Value позволяет внедрять значения из application.properties непосредственно в код.
                                   Вы можете использовать её над полями, методами или даже параметрами методов.
                            """, "NameAuthor.SurNameAuthor", LocalDateTime.now().toString()));
            newEntities.add(new NewsEntity(7, "New's № 7",
                    """
                            @ConfigurationProperties позволяет связать значения из файлов application.properties
                                                     с полями класса в вашем приложении.
                            """, "NameAuthor.SurNameAuthor", LocalDateTime.now().toString()));
            newsService.batchInsert(newEntities);
        }
    }
}
