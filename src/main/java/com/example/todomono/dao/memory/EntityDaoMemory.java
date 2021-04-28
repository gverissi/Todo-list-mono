package com.example.todomono.dao.memory;

import com.example.todomono.dao.EntityDaoInterface;
import com.example.todomono.exception.DaoEntityNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EntityDaoMemory<Entity> implements EntityDaoInterface<Entity> {

    protected final Map<Long, Entity> entityMap = new HashMap<>();

    public EntityDaoMemory() {
    }

    @Override
    public List<Entity> findAll() {
        return new ArrayList<>(entityMap.values());
    }

    @Override
    public Entity findById(long id) throws DaoEntityNotFoundException {
        Entity entity = entityMap.get(id);
        if (entity == null) throw new DaoEntityNotFoundException();
        return entity;
    }

    @Override
    public void deleteById(long id) {
        entityMap.remove(id);
    }

    @Override
    public Entity getOne(long id) throws DaoEntityNotFoundException {
        Entity entity = entityMap.get(id);
        if (entity == null) throw new DaoEntityNotFoundException();
        return entity;
    }

}
