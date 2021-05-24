package com.example.todomono.dao;

import com.example.todomono.entity.Role;

public interface RoleDaoInterface extends EntityDaoInterface<Role> {

    /**
     * Find a Role according to it's name.
     * If the Role doesn't exist Throw an DaoEntityNotFoundException and thus the 404 error page is displayed.
     * @param roleName Role's name.
     * @return The Role entity.
     */
    Role findByRoleName(String roleName);

}
