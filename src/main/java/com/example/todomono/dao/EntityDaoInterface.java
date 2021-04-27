package com.example.todomono.dao;

import com.example.todomono.exception.DaoConstraintViolationException;
import com.example.todomono.exception.DaoEntityNotFoundException;

import java.util.List;

public interface EntityDaoInterface<Entity> {

    Entity save(Entity entity) throws DaoConstraintViolationException;

    List<Entity> findAll();

    Entity findById(long id) throws DaoEntityNotFoundException;

    void deleteById(long id);

    Entity getOne(long id) throws DaoEntityNotFoundException;

}
