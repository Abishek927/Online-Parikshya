package com.online.exam.security;

import com.online.exam.dto.AuthorityDto;
import com.online.exam.dto.RoleDto;
import com.online.exam.dto.UserDto;
import com.online.exam.model.Authority;
import com.online.exam.model.Role;
import com.online.exam.repo.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import java.util.stream.Collectors;


public class UserPrincipal implements UserDetails {
    @Autowired
    private UserDto userDto;
    @Autowired
    private RoleRepo roleRepo;

    public UserPrincipal(UserDto userDto,RoleRepo roleRepo){
        super();
        this.userDto=userDto;
        this.roleRepo=roleRepo;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role retrievedRole=roleRepo.findByRoleName(userDto.getRoleName());
        List<Role> roles=new ArrayList<>();
        roles.add(retrievedRole);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (Role eachRole:roles
             ) {
            authorities.addAll(eachRole.getAuthorities().stream().map(a -> new SimpleGrantedAuthority(a.getAuthorityName()))
                    .collect(Collectors.toSet()));

        }


        return authorities;
    }

    @Override
    public String getPassword() {
        return userDto.getUserPassword();
    }

    @Override
    public String getUsername() {
        return this.userDto.getUserEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
