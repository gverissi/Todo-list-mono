package com.example.todomono.dao;

import com.example.todomono.entity.Role;

import java.util.List;

public interface RoleDaoInterface {

    Role save(Role role);

    Role findById(int id);

    List<Role> findAll();

    Role findByRoleName(String roleName);
}
