package com.fluxing.publish;

import com.fluxing.model.TaskModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
@Slf4j
public class TaskPublisher {

    private final Sinks.Many<TaskModel> projectModelUpdates;

    public TaskPublisher() {
        this.projectModelUpdates = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(TaskModel task) {
        log.info("Init method publishTask in class ApplicationPublisher at system time: {}", System.currentTimeMillis());
        projectModelUpdates.tryEmitNext(task);
    }

    public Sinks.Many<TaskModel> getUpdatesSink() {
        return projectModelUpdates;
    }

}
