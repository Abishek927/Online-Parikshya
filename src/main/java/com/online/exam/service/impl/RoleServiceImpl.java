package com.online.exam.service.impl;

import com.online.exam.dto.AuthorityDto;
import com.online.exam.dto.RoleDto;
import com.online.exam.model.Authority;
import com.online.exam.model.Role;
import com.online.exam.repo.AuthorityRepo;
import com.online.exam.repo.RoleRepo;
import com.online.exam.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AuthorityRepo authorityRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Role resultRole=null;
        Role role=new Role();
        role.setRoleName(roleDto.getName().toUpperCase());
        role.setAuthorities(roleDto.getAuthoritySet().stream().map(a->{
            Authority authority=authorityRepo.findByAuthorityName(a.getName());
            if(authority==null){
                authority=authorityRepo.save(new Authority(a.getName()));
            }
            return authority;


        }).collect(Collectors.toSet()));
         resultRole=roleRepo.save(role);
         RoleDto roleDto1=new RoleDto();
         roleDto1.setName(resultRole.getRoleName());
         roleDto1.setAuthoritySet(role.getAuthorities().stream().map(authority -> this.modelMapper.map(authority,AuthorityDto.class)).collect(Collectors.toSet()));

        return roleDto1;
    }

    @Override
    public RoleDto updateRole(RoleDto roleDto, Long roleId) throws Exception {
        Role role=this.roleRepo.findById(roleId).get();
        Role retrievedRole=new Role();
        if(roleDto.getName()!=null) {
            retrievedRole = this.roleRepo.findByRoleName(roleDto.getName());
            if(retrievedRole.equals(null)){
                role.setRoleName(roleDto.getName());
            }
        }
        Set<AuthorityDto> authoritySet =roleDto.getAuthoritySet();


        if(!authoritySet.isEmpty()) {
            role.setAuthorities(roleDto.getAuthoritySet().stream().map(a -> {
                Authority authority = authorityRepo.findByAuthorityName(a.getName());
                if (authority == null) {
                    authority = authorityRepo.save(new Authority(a.getName()));
                } else {
                    try {
                        throw new Exception("authority  with the given name already defined!!!");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                return authority;

            }).collect(Collectors.toSet()));
        }
        Role resultRole = roleRepo.save(role);
        RoleDto roleDto1=new RoleDto();
        roleDto1.setName(resultRole.getRoleName());
        roleDto1.setAuthoritySet(resultRole.getAuthorities().stream().map(authority -> this.modelMapper.map(authority,AuthorityDto.class)).collect(Collectors.toSet()));
            return roleDto1;
    }
}
