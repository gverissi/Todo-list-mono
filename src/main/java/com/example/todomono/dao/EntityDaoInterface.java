package com.example.todomono.dao;

import java.util.List;

public interface EntityDaoInterface<Entity> {

    Entity save(Entity entity);

    List<Entity> findAll();

    Entity findById(int id);

    void deleteById(int id);

    Entity getOne(int id);

}
