package com.skillbox.fibonacci;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class FibonacciControllerTest extends PostgresTestContainerInitializer {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test get request with status code 2xx(Successful).")
    public void test1GetNumber() throws Exception {
        getMockMvc().perform(MockMvcRequestBuilders.get("/fibonacci/1")).
                andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Test get request with status code 4xx(ClientError).")
    public void test2GetNumber() throws Exception {
        getMockMvc().perform(MockMvcRequestBuilders.get("/fibonacci/-1")).
                andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    private MockMvc getMockMvc() {
        return mockMvc;
    }
}
