package com.github.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryBranchDto {
    private String repositoryName;
    private String ownerLogin;
    private List<BranchDto> branch;
}
