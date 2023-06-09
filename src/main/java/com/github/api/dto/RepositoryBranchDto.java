package com.github.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryBranchDto {
    private String name;
    private String owner;
    private List<BranchDto> branchDtoList;
}
