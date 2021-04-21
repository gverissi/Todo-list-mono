package com.example.todomono.dao.database;

import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.entity.Role;
import com.example.todomono.repository.RoleRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleDaoDb implements RoleDaoInterface {

    private final RoleRepositoryInterface roleRepository;

    @Autowired
    public RoleDaoDb(RoleRepositoryInterface roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findById(int id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

}
