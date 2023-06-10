package com.github.api.controller;

import com.github.api.dto.BranchDto;
import com.github.api.dto.RepositoryBranchDto;
import com.github.api.dto.RepositoryDto;
import com.github.api.service.GithubService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class GithubController {
    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<RepositoryDto>> getAllRepositories(@PathVariable String username){
        return githubService.getAllRepos(username);
    }

    @GetMapping(value = "/{username}/{repo}/branches")
    public Mono<List<BranchDto>> getAllBranches(@PathVariable String username, @PathVariable String repo){
        return githubService.getAllBranches(username, repo);
    }

    @GetMapping(value = "/{username}/combined")
    public Mono<List<RepositoryBranchDto>> getRepositoriesAndBranches(@PathVariable String username){
        return githubService.getRepositoryBranchCombined(username);
    }
}
