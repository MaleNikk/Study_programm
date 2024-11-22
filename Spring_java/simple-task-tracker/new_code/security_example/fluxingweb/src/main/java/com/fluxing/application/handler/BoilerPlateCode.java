package com.fluxing.application.handler;

import com.fluxing.application.entity.Task;
import com.fluxing.application.entity.User;
import com.fluxing.application.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.Objects;

@Component
@Slf4j
public final class  BoilerPlateCode {

    public static Mono<ServerResponse> error() {
        log.info("Init error from BoilerPlateCode.");
        return ServerResponse.badRequest().contentType(MediaType
                .valueOf("We caught exception! Please try again later!")).build();
    }

    public static Mono<ServerResponse> getDataUserById(String search, ApplicationService<?> service){
        log.info("Init getDataUserById from BoilerPlateCode.");
        return search == null ?  error() : ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).
                body(Objects.requireNonNull(service.findById(search)), User.class);
    }

    public static Mono<ServerResponse> getDataAllUsers(ApplicationService<?> service){
        log.info("Init getDataAllUsers from BoilerPlateCode.");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).
                body(Objects.requireNonNull(service.findAll()), User.class);
    }

    public static Mono<ServerResponse> getDataUserByName(String search, ApplicationService<?> service){
        log.info("Init getDataUserByName from BoilerPlateCode.");
        return search == null ? error() : ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).
                body(Objects.requireNonNull(service.findByName(search)), User.class);
    }


    public static Mono<ServerResponse> getDataTaskById(String search, ApplicationService<?> service){
        log.info("Init getDataTaskById from BoilerPlateCode.");
        return search == null ? error() : ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).
                body(Objects.requireNonNull(service.findById(search)), Task.class);
    }

    public static Mono<ServerResponse> getDataAllTasks(ApplicationService<?> service){
        log.info("Init getDataAllTasks from BoilerPlateCode.");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).
                body(Objects.requireNonNull(service.findAll()), Task.class);
    }

    public static Mono<ServerResponse> getDataTaskByName(String search, ApplicationService<?> service){
        log.info("Init getDataTasksByName from BoilerPlateCode.");
        return search == null ? error() : ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).
                body(Objects.requireNonNull(service.findByName(search)), Task.class);
    }
}
