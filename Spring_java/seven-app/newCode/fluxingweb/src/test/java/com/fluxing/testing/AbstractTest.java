package com.fluxing.testing;

import com.fluxing.entity.Task;
import com.fluxing.entity.TaskStatus;
import com.fluxing.model.TaskModel;
import com.fluxing.model.UserModel;
import com.fluxing.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@AutoConfigureWebTestClient
public class AbstractTest {

    protected static final String
            FIRST_ITEM_ID = UUID.randomUUID().toString(),
            SECOND_ITEM_ID = UUID.randomUUID().toString();
    protected static final Instant
            CREATE_AT = Instant.now(),
            UPDATE_AT = CREATE_AT.plusMillis(100000L);
    protected static final UserModel
            FULL_USER = new UserModel("1", "2", "3", Set.of("1", "2", "3")),
            LIGHT_USER = new UserModel("3", "2", "1", Set.of("3", "2", "1")),
            TEST_MODEL = new UserModel("test_id", "test_name", "test_email", Set.of("3", "2", "1"));
    protected static final Set<UserModel> listUsers = Set.of(FULL_USER, LIGHT_USER);
    protected static final TaskModel
            LIGHT_TASK = new TaskModel(),
            FULL_TASK = new TaskModel(SECOND_ITEM_ID, "2", "3", "4", "5",
                    CREATE_AT, UPDATE_AT, TaskStatus.IN_PROGRESS, listUsers),
            MODEL_TEST = new TaskModel(FIRST_ITEM_ID,"test_@","test_#",
                    null,null,null,null,null,null);
    protected static final Set<TaskModel> LIST_TASKS = Set.of(FULL_TASK, LIGHT_TASK);

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected TaskRepository testTaskRepository;

    @BeforeEach
    public void setup() {
        testTaskRepository.saveAll(List.of( new Task(), new Task())).collectList().block();
    }

    @AfterEach
    public void afterEach(){
        testTaskRepository.deleteAll().block();
    }
}
