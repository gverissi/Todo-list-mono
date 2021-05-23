package com.example.todomono.dao.memory;

import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.entity.Role;
import com.example.todomono.exception.DaoConstraintViolationException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Profile("memory")
public class RoleDaoMemory extends EntityDaoMemory<Role> implements RoleDaoInterface {

    private static long lastId = 0;

    public RoleDaoMemory() {
        super();
    }

    @Override
    public Role save(Role entity) throws DaoConstraintViolationException {
        long entityId = entity.getId();
        String uniqueColumn = entity.getRoleName();
        List<Role> entities = findAll();
        boolean newEntity = false;
        if (!entityMap.containsKey(entityId)) {
            entityId = getNextId();
            newEntity = true;
        } else {
            entities.remove(entity);
        }
        boolean constraintViolation = entities.stream().anyMatch(todoList -> todoList.getRoleName().equals(uniqueColumn));
        if (constraintViolation) {
            if (newEntity) lastId--;
            throw new DaoConstraintViolationException();
        }
        entity.setId(entityId);
        entityMap.put(entityId, entity);
        return entity;
    }

    @Override
    public Role findByRoleName(String roleName) {
        return entityMap.values().stream().filter(role -> role.getRoleName().equals(roleName)).findFirst().orElse(null);
    }

    private long getNextId() {
        return ++lastId;
    }

}
