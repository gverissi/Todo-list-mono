package com.example.todomono.dao;

import com.example.todomono.entity.Role;

public interface RoleDaoInterface extends EntityDaoInterface<Role> {

    Role findByRoleName(String roleName);

}
