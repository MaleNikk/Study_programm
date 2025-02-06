package searchengine.controllers;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import searchengine.configuration.TestConfiguration;
import java.io.IOException;

public class TestApiController extends TestConfiguration {

    @Test
    @DisplayName("Test call update statistics")
    public void whenCallStatistics_thenReturnCodeOk() throws Exception {
        testModel("statistics", HttpStatus.OK.value());
        System.out.println("\nTest call update statistics complete successful.");
    }

    @Test
    @DisplayName("Test call empty search query")
    public void whenCallEmptySearch_thenReturnCodeOk() throws Exception {
        testModel("search", HttpStatus.BAD_REQUEST.value());
        System.out.println("\nTest call empty search query complete successful.");
    }

    @Test
    @DisplayName("Test call start indexing")
    public void whenCallStartIndexing_thenReturnCodeOk() throws Exception {
        testModel("startIndexing",HttpStatus.OK.value());
        System.out.println("\nTest call start indexing complete successful.");
    }

    @Test
    @DisplayName("Test call stop indexing")
    public void whenCallStopIndexing_thenReturnCodeOk() throws Exception {
        testModel("stopIndexing",HttpStatus.OK.value());
        System.out.println("\nTest call stop indexing complete successful.");
    }

    @Test
    @DisplayName("Test call add nullable page for indexing")
    public void whenCallEmptyIndexPage_thenReturnStatistics() throws Exception {
        testModel("indexPage",HttpStatus.BAD_REQUEST.value());
        System.out.println("\nTest call nullable page for indexing complete successful.");
    }

    private void testModel(String path, int code) throws IOException {
        String uri = String.format("http://localhost:%d/api/%s",serverPort,path);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(uri).build();
        Call call = client.newCall(request);
        Response response = call.execute();
        Assertions.assertEquals(code, response.code());
        response.close();
        System.out.printf("Test result:\n\t-expected code: %d\n\t-actual code: %d\nSuccessful!", code,response.code());
    }
}
