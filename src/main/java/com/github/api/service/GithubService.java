package com.github.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.api.dto.BranchDto;
import com.github.api.dto.RepositoryBranchDto;
import com.github.api.dto.RepositoryDto;
import com.github.api.exception.NoSuchUserExistsException;
import com.github.api.exception.UnsupportedHeaderException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


//todo Error response for 406 needs modification
@Service
public class GithubService {

    private final Logger logger = LoggerFactory.getLogger(GithubService.class);
    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    public GithubService(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }


    public Mono<List<RepositoryDto>> getAllRepos(String username) {
        if(username.isBlank()){
            return Mono.error(new NoSuchUserExistsException("User must not be blank"));
        }
        return webClient.get()
                .uri("/users/" + username + "/repos")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> Mono.error(new NoSuchUserExistsException(
                        "No such user with username: "+username+" exists")))
                .onStatus(HttpStatus.NOT_ACCEPTABLE::equals,
                        response -> Mono.error(new UnsupportedHeaderException(
                                    "Unsupported header, the accepted header should be Accept: application/json")))
                .bodyToFlux(String.class)
                .concatMap(repoJson -> mapToRepositoryDto(repoJson, username))
                .collectList();
    }

    private Flux<RepositoryDto> mapToRepositoryDto(String repoJson, String username) {
        try {
            JsonNode jsonNode = objectMapper.readTree(repoJson);

            return Flux.fromIterable(jsonNode)
                    .filter(node -> !node.get("fork").asBoolean())
                    .map(node -> {
                        String repoName = node.get("name").asText();
                        RepositoryDto repositoryDto = new RepositoryDto();
                        repositoryDto.setName(repoName);
                        repositoryDto.setLogin(username);
                        return repositoryDto;
                    });
        } catch (JsonProcessingException e) {
            logger.error("Cannot parse Json", e);
            return Flux.error(new IllegalArgumentException("Error occurred"));
        }
    }


    public Mono<List<BranchDto>> getAllBranches(String username, String repo) {
        if(username.isBlank()){
            return Mono.error(new NoSuchUserExistsException("User must not be blank"));
        }
        if(repo.isBlank()){
            return Mono.error(new NoSuchUserExistsException("Repo must not be blank"));
        }
        return webClient.get()
                .uri("/repos/" + username + "/" + repo + "/branches")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> Mono.error(new NoSuchUserExistsException(
                                "No such user with username: "+username+" exists")))
                .onStatus(HttpStatus.NOT_ACCEPTABLE::equals,
                        response -> Mono.error(new UnsupportedHeaderException(
                                "Unsupported header the accepted header should be Accept: application/json")))
                .bodyToFlux(String.class)
                .concatMap(branchJson -> mapToBranchDto(branchJson))
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
            logger.error("Cannot parse Json", e);
            return Flux.error(new IllegalArgumentException("Error occurred"));
        }
    }

    public Mono<List<RepositoryBranchDto>> getRepositoryBranchCombined(String username) {
        return getAllRepos(username)
                .flatMapIterable(repos -> repos)
                .flatMap(repo -> getAllBranches(username, repo.getName())
                        .map(branches -> {
                            RepositoryBranchDto repoBranchDto = new RepositoryBranchDto();
                            repoBranchDto.setRepositoryName(repo.getName());
                            repoBranchDto.setOwnerLogin(repo.getLogin());
                            repoBranchDto.setBranch(branches);
                            return repoBranchDto;
                        })
                )
                .collectList();
    }
}
