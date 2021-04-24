package com.example.todomono.service;

import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleDaoInterface roleDao;

    @Autowired
    public RoleService(RoleDaoInterface roleDao) {
        this.roleDao = roleDao;
    }

    public Role createRole(String roleName) {
        Role role = new Role(roleName);
        return roleDao.save(role);
    }

    public List<Role> findAll() {
        return roleDao.findAll();
    }

    public Role getOne(long id) {
        return roleDao.getOne(id);
    }

    public Role findByRoleName(String roleName) {
        return roleDao.findByRoleName(roleName);
    }
}
