package searchengine.config;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Slf4j
public class AppProperties {

    private boolean status;

    private int activeThreads;

    public AppProperties(
            @Value("${indexing-settings.morphology.status}")boolean status,
            @Value("${indexing-settings.morphology.active-threads}")int activeThreads) {
        this.status = status;
        this.activeThreads = activeThreads;
        log.info("Init status morphology: {}. Count threads initialize: {}.", isStatus(), getActiveThreads());
    }
}
