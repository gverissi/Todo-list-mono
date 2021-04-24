package com.example.todomono.dao.memory;

import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.entity.Role;
import com.example.todomono.exception.EntityNotFoundException;

import java.util.*;

public class RoleDaoMemory implements RoleDaoInterface {

    private static long LAST_ID = 0;
    private final Map<Long, Role> entityMap = new HashMap<>();

    @Override
    public Role save(Role entity) {
        long id = entity.getId();
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
    public Role findById(long id) {
        Role entity = entityMap.get(id);
        if (entity == null) throw new EntityNotFoundException("Role entity with id = " + id + " do not exist!");
        return entity;
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public Role getOne(long id) {
        return null;
    }

    @Override
    public List<Role> findAll() {
        return new ArrayList<>(entityMap.values());
    }

    @Override
    public Role findByRoleName(String roleName) {
        return null;
    }

    private long getNewId() {
        return ++LAST_ID;
    }

}
