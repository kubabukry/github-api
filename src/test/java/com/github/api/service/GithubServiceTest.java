package com.github.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.api.dto.RepositoryDto;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.List;


@ExtendWith(MockitoExtension.class)
class GithubServiceTest {

    private MockWebServer mockWebServer;
    private GithubService githubService;

    @BeforeEach
    void setupMockWebServer() {
        mockWebServer = new MockWebServer();
        githubService = new GithubService(WebClient.create(), new ObjectMapper());
    }

    @Test
    void test() {
        String json = "[{\"name\": \"cinema-application\", \"login\": \"kubabukry\"}, {\"name\": \"github-api\", \"login\": \"kubabukry\"}]";

        List<RepositoryDto> result = List.of(
                new RepositoryDto("cinema-application", "kubabukry"),
                new RepositoryDto("github-api", "kubabukry")
        );
        mockWebServer.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );
        Mono<List<RepositoryDto>> allRepos = githubService.getAllRepos("kuba");

        assertEquals(allRepos.block(), result);
    }
}