package com.online.exam.service.impl;

import com.online.exam.dto.RoleDto;
import com.online.exam.model.Authority;
import com.online.exam.model.Role;
import com.online.exam.repo.AuthorityRepo;
import com.online.exam.repo.RoleRepo;
import com.online.exam.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AuthorityRepo authorityRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Role role=new Role();
        role.setRoleName(roleDto.getName());
        role.setAuthorities(roleDto.getAuthoritySet().stream().map(a->{
            Authority authority=authorityRepo.findbyAuthorityName(a.getName());
            if(authority==null){
                authority=authorityRepo.save(new Authority(a.getName()));
            }
            return authority;

        }).collect(Collectors.toSet()));
        Role resultRole=roleRepo.save(role);
        return modelMapper.map(resultRole,RoleDto.class);
    }
}
