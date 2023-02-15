package com.online.exam.service;

import com.online.exam.dto.RoleDto;

public interface RoleService {
    RoleDto createRole(RoleDto  roleDto);
    RoleDto updateRole(RoleDto roleDto,Long roleId) throws Exception;
}
