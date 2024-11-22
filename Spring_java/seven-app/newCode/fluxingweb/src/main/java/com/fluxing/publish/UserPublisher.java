package com.fluxing.publish;

import com.fluxing.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
@Slf4j
public class UserPublisher {

    private final Sinks.Many<UserModel> projectModelUpdates;

    public UserPublisher() {
        this.projectModelUpdates = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(UserModel user) {
        log.info("Init method publishTask in class ApplicationPublisher at system time: {}", System.currentTimeMillis());
        projectModelUpdates.tryEmitNext(user);
    }

    public Sinks.Many<UserModel> getUpdatesSink() {
        return projectModelUpdates;
    }
}
