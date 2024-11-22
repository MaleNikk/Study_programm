package com.fluxing.application.config;

import com.fluxing.application.handler.HandlerTask;
import com.fluxing.application.handler.HandlerUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class ApplicationRouter {

    @Bean
    public RouterFunction<ServerResponse> taskRouters(HandlerTask handlerTask) {
        log.info("Init method taskRouters in class ApplicationRouter at system time: {}", System.currentTimeMillis());
        return RouterFunctions.route()
                .GET("/webflux/test/task/all", handlerTask::getAll)
                .GET("/webflux/test/task/{id}", handlerTask::findById)
                .GET("/webflux/test/task/name", handlerTask::findByName)
                .POST("/webflux/test/task/add", handlerTask::create)
                .PUT("/webflux/test/task/update/{id}",handlerTask::update)
                .DELETE("/webflux/test/task/delete/{id}", handlerTask::deleteById)
                .GET("/webflux/test/error", handlerTask::errorRequest)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userRouters(HandlerUser handlerUser) {
        log.info("Init method userRouters in class ApplicationRouter at system time: {}", System.currentTimeMillis());
        return RouterFunctions.route()
                .GET("/webflux/test/user/all", handlerUser::getAll)
                .GET("/webflux/test/user/{id}", handlerUser::findById)
                .GET("/webflux/test/user/name", handlerUser::findByName)
                .POST("/webflux/test/user/add", handlerUser::create)
                .PUT("/webflux/test/user/update/{id}",handlerUser::update)
                .DELETE("/webflux/test/user/delete/{id}", handlerUser::deleteById)
                .build();
    }
}
