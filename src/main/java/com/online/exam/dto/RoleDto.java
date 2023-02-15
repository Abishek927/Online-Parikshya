package com.online.exam.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class RoleDto {
    private String name;
    private Set<AuthorityDto> authoritySet;
}
