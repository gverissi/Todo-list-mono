package com.example.todomono.service;

import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleService {

    @Autowired
    private final RoleDaoInterface roleDao;

    public RoleService(RoleDaoInterface roleDao) {
        this.roleDao = roleDao;
    }

    public Role createRole(String roleName) {
        Role role = new Role(roleName);
        return roleDao.save(role);
    }

}
