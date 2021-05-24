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

    /**
     * Create a new Role. Example 'ADMIN' or 'USER'.
     * The roleName must be unique otherwise throw a DaoConstraintViolationException that Extends RuntimeException.
     * @param roleName A unique name For the Role.
     * @return The created Role entity.
     */
    public Role createRole(String roleName) {
        Role role = new Role(roleName);
        return roleDao.save(role);
    }

    /**
     * Find all Roles.
     * @return List of Roles.
     */
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    /**
     * Get one role by it's id. If the role doesn't exist throw a DaoEntityNotFoundException and the 404 error page is displayed.
     * This method doesn't actually hit the database, it just return a reference of the Role entity.
     * A call to the database is done only if the attributes are accessed.
     * This method is useful if you just want to add a role to a Customer.
     * @param id Role's id.
     * @return the Role entity.
     */
    public Role getOne(long id) {
        return roleDao.getOne(id);
    }

    /**
     * Find one role by it's name. If the role doesn't exist throw a DaoEntityNotFoundException and the 404 error page is displayed.
     * @param roleName Role's name.
     * @return the Role entity.
     */
    public Role findByRoleName(String roleName) {
        return roleDao.findByRoleName(roleName);
    }
}
