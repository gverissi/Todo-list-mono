package com.example.todomono.dao;

import java.util.List;

public interface EntityDaoInterface<Entity> {

    Entity save(Entity entity);

    List<Entity> findAll();

    Entity findById(long id);

    void deleteById(long id);

    Entity getOne(long id);

}
