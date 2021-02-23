package com.example.todomono.dao;

import com.example.todomono.entity.Role;

import java.util.Set;

public interface RoleDaoInterface {

    Role save(Role role);

    Role findById(int id);

    Set<Role> findAll();

}
