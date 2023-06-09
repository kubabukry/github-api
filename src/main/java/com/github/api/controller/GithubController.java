package com.github.api.controller;

import com.github.api.dto.RepositoryDto;
import com.github.api.service.GithubService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class GithubController {
    private final GithubService githubService;

    public GithubController(GithubService githubService, WebClient githubWebClient) {
        this.githubService = githubService;
    }

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<RepositoryDto>> getAllRepositories(@PathVariable String username){
        return githubService.getAllRepos(username);
    }
}
