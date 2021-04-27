package com.example.todomono.dao.memory;

import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.entity.Role;
import com.example.todomono.exception.DaoConstraintViolationException;
import com.example.todomono.exception.DaoEntityNotFoundException;

import java.util.*;

public class RoleDaoMemory implements RoleDaoInterface {

    private static long LAST_ID = 0;
    private final Map<Long, Role> entityMap = new HashMap<>();

    @Override
    public Role save(Role entity) throws DaoConstraintViolationException {
        long id = entity.getId();
        if (!entityMap.containsKey(id)) {
            id = getNewId();
            entity.setId(id);
            if (entityMap.values().stream().anyMatch(role -> role.getRoleName().equals(entity.getRoleName()))) {
                throw new DaoConstraintViolationException();
            }
        }
        entityMap.put(id, entity);
        return entity;
    }

    @Override
    public Role findById(long id) throws DaoEntityNotFoundException {
        Role entity = entityMap.get(id);
        if (entity == null) throw new DaoEntityNotFoundException();
        return entity;
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public Role getOne(long id) throws DaoEntityNotFoundException {
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
