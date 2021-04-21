package com.example.todomono.dao.memory;

import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.entity.Role;
import com.example.todomono.exception.EntityNotFoundException;

import java.util.*;

public class RoleDaoMemory implements RoleDaoInterface {

    private static int LAST_ID = 0;
    private final Map<Integer, Role> entityMap = new HashMap<>();

    @Override
    public Role save(Role entity) {
        int id = entity.getId();
        if (!entityMap.containsKey(id)) {
            id = getNewId();
            entity.setId(id);
            if (entityMap.values().stream().anyMatch(role -> role.getRoleName().equals(entity.getRoleName()))) {
                throw new RuntimeException("Unique constrain violation: a Role entity with roleName = " + entity.getRoleName() + " already exist!");
            }
        }
        entityMap.put(id, entity);
        return entity;
    }

    @Override
    public Role findById(int id) {
        Role entity = entityMap.get(id);
        if (entity == null) throw new EntityNotFoundException("Role entity with id = " + id + " do not exist!");
        return entity;
    }

    @Override
    public List<Role> findAll() {
        return new ArrayList<>(entityMap.values());
    }

    @Override
    public Role findByRoleName(String roleName) {
        return null;
    }

    private int getNewId() {
        return ++LAST_ID;
    }

}
