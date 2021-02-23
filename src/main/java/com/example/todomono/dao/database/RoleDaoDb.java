package com.example.todomono.dao.database;

import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.entity.Role;
import com.example.todomono.repository.RoleRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class RoleDaoDb implements RoleDaoInterface {

    @Autowired
    private final RoleRepositoryInterface roleRepository;

    public RoleDaoDb(RoleRepositoryInterface roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findById(int id) {
        return null;
    }

    @Override
    public Set<Role> findAll() {
        return null;
    }

}
