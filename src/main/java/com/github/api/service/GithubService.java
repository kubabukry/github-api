package com.github.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.api.dto.BranchDto;
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

            return Flux.fromIterable(jsonNode)
                    .filter(node -> node.has("fork") && !node.get("fork").asBoolean())
                    .map(node -> {
                        String repoName = node.get("name").asText();
                        RepositoryDto repositoryDto = new RepositoryDto();
                        repositoryDto.setName(repoName);
                        repositoryDto.setLogin(username);
                        return repositoryDto;
                    });
        } catch (JsonProcessingException e) {
            return Flux.error(e);
        }
    }


    public Mono<List<BranchDto>> getAllBranches(String username, String repo){
        return webClient.get()
                .uri("/repos/"+username+"/"+repo+"/branches")
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(branchJson -> mapToBranchDto(branchJson))
                .collectList();
    }

private Flux<BranchDto> mapToBranchDto(String branchJson) {
    try {
        JsonNode jsonNode = objectMapper.readTree(branchJson);

        return Flux.fromIterable(jsonNode)
                .map(node -> {
                    String branchName = node.get("name").asText();
                    String lastCommitSha = node.get("commit").get("sha").asText();

                    BranchDto branchDto = new BranchDto();
                    branchDto.setName(branchName);
                    branchDto.setLastCommitSha(lastCommitSha);

                    return branchDto;
                });
    } catch (JsonProcessingException e) {
        return Flux.error(e);
    }
}
}
