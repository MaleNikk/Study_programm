package com.fluxing.testing;

import com.fluxing.model.TaskModel;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Set;

class WebApplicationTests extends AbstractTest {

    @Test
    public void test1_getAll_viewListFromDB() {

        webTestClient.post().uri("/webflux/test/task/add").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(LIGHT_TASK), TaskModel.class).exchange().expectStatus().isCreated();
        webTestClient.post().uri("/webflux/test/task/add").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(TEST_MODEL), TaskModel.class).exchange().expectStatus().isCreated();
        webTestClient.post().uri("/webflux/test/task/add").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(FULL_TASK), TaskModel.class).exchange().expectStatus().isCreated();

        webTestClient
                .get()
                .uri("/webflux/test/task/all")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void test2_getByID_returnItemById() {

        webTestClient.post().uri("/webflux/test/task/add").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(FULL_TASK), TaskModel.class).exchange().expectStatus().isCreated();

        webTestClient
                .get()
                .uri("/webflux/test/task/{id}", SECOND_ITEM_ID)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test3_getByName_returnItemByName() {

        webTestClient.post().uri("/webflux/test/task/add").contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(LIGHT_TASK), TaskModel.class).exchange().expectStatus().isCreated();

        webTestClient.get()
                .uri("/webflux/test/task/name?name=")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test4_createItem_returnNewItemWithPublishEvent() {

        LIGHT_TASK.setName("Test task");
        LIGHT_TASK.setAuthorId("Test author_id");
        LIGHT_TASK.setObserverIds(Set.of("3", "2", "1"));

        webTestClient
                .post()
                .uri("/webflux/test/task/add")
                .body(Mono.just(LIGHT_TASK), TaskModel.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated();

        webTestClient
                .get()
                .uri("/webflux/test/task/name", LIGHT_TASK.getName())
                .accept(MediaType.valueOf(MediaType.TEXT_PLAIN_VALUE)).exchange()
                .expectStatus().isOk();

        webTestClient
                .get()
                .uri("/webflux/test/task/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(new ParameterizedTypeReference<ServerSentEvent<TaskModel>>() {
                })
                .getResponseBody()
                .take(1L)
                .as(StepVerifier::create);

    }

    @Test
    public void test5_updateItem_returnUpdatedItem() {

        webTestClient
                .get()
                .uri("/webflux/test/task/add", MODEL_TEST)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient
                .put()
                .uri("/webflux/test/task/update/{id}", FIRST_ITEM_ID)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(MODEL_TEST), TaskModel.class)
                .exchange()
                .expectStatus().isOk();

        webTestClient
                .get()
                .uri("/webflux/test/task/{id}", FIRST_ITEM_ID)
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK);


        StepVerifier.create(testTaskRepository.findByName("test_#"))
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    public void test6_deleteById_returnItemsWithoutDeletedItem() {
        webTestClient.delete()
                .uri("/webflux/test/task/delete/{id}", FIRST_ITEM_ID)
                .exchange()
                .expectStatus().isOk();
    }
}
