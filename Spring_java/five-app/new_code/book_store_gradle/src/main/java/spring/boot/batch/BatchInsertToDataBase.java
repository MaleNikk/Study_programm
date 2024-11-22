package spring.boot.batch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import spring.boot.dto.ApplicationEntity;
import spring.boot.injector.ServiceManagement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@RequiredArgsConstructor
@Component
@Slf4j
public class BatchInsertToDataBase {

    private final ServiceManagement<ApplicationEntity> serviceManagement;

    @EventListener(ApplicationStartedEvent.class)
    public void createDataBooks(){
        List<ApplicationEntity> entities = serviceManagement.viewAll("books");
        if (entities.isEmpty()) {
            log.info("Generate default books.");
            entities = new ArrayList<>();
            int indexBook = 1;
            int indexCategory = 1;
            AtomicInteger price = new AtomicInteger(1500);
            do {
                ApplicationEntity application = new ApplicationEntity();
                int indexField = 0;
                application.getIntegers()[indexField] = indexBook;
                application.getStrings()[indexField] = "Some author - ".concat(String.valueOf(indexBook));
                indexField++;
                application.getIntegers()[indexField] = price.getAndAdd(100);
                application.getStrings()[indexField] = "Title about this book - ".concat(String.valueOf(indexBook));
                indexField++;
                application.getIntegers()[indexField] = indexCategory;
                application.getStrings()[indexField] = String.valueOf(LocalDateTime.now());
                entities.add(application);
                indexBook++;
                indexCategory++;
                if (indexCategory == 10) {
                    indexCategory = 1;
                }
            } while (entities.size() <= 25);
            getServiceManagement().batchInsert(entities, "books");
        } else {
            log.info("Default books are present in database.");
        }
    }

    @EventListener(ApplicationStartedEvent.class)
    public void createDataCategory(){
        List<ApplicationEntity> entities = serviceManagement.viewAll("category");
        if (entities.isEmpty()) {
            log.info("Generate default categories.");
            entities = new ArrayList<>();
            int indexCategory = 1;
            do {
                ApplicationEntity application = new ApplicationEntity();
                int indexField = 0;
                application.getIntegers()[indexField] = indexCategory;
                application.getStrings()[indexField] = "Some category - ".concat(String.valueOf(indexCategory));
                indexField++;
                application.getStrings()[indexField] = "Description about this category - "
                        .concat(String.valueOf(indexCategory));
                indexCategory++;
                entities.add(application);
            } while (entities.size() <= 10);
            getServiceManagement().batchInsert(entities, "category");
        } else {
            log.info("Default categories are present in database.");
        }
    }
}
