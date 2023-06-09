package com.github.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.api.dto.RepositoryDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GithubService {

    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    public GithubService(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public Mono<List<RepositoryDto>> getAllRepos(String username) {
        return webClient.get()
                .uri("/users/" + username + "/repos")
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(repoJson -> mapToRepositoryDto(repoJson, username))
                .collectList();
    }

    private Flux<RepositoryDto> mapToRepositoryDto(String repoJson, String username) {
        try {
            JsonNode jsonNode = objectMapper.readTree(repoJson);
            List<JsonNode> repoNodes = jsonNode.findValues("name");

            return Flux.fromIterable(repoNodes)
                    .map(node -> {
                        String repoName = node.asText();
                        RepositoryDto repositoryDto = new RepositoryDto();
                        repositoryDto.setName(repoName);
                        repositoryDto.setLogin(username);
                        return repositoryDto;
                    });
        } catch (JsonProcessingException e) {
            return Flux.error(e);
        }
    }
}
