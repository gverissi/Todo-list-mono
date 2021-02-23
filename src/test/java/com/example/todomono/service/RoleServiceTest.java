package com.example.todomono.service;

import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.entity.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Test
    void createRole() {
        RoleDaoInterface roleDaoMock = mock(RoleDaoInterface.class);
        Role role = new Role("USER");
        when(roleDaoMock.save(role)).thenReturn(role);
        RoleService roleService = new RoleService(roleDaoMock);
        Role createdRole = roleService.createRole("USER");
        assertEquals(role, createdRole);
        verify(roleDaoMock).save(createdRole);
    }

}