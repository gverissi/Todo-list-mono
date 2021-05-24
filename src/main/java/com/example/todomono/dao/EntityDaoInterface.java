package com.example.todomono.dao;

import com.example.todomono.exception.DaoConstraintViolationException;
import com.example.todomono.exception.DaoEntityNotFoundException;

import java.util.List;

public interface EntityDaoInterface<Entity> {

    /**
     * Persist an Entity.
     * @param entity The Entity to be persisted.
     * @return The persisted Entity.
     * @throws DaoConstraintViolationException If the Entity is not unique.
     */
    Entity save(Entity entity) throws DaoConstraintViolationException;

    /**
     * Find all Entities.
     * @return A List of Entities.
     */
    List<Entity> findAll();

    /**
     * Find an Entity according to it's id.
     * If the Entity doesn't exist Throw an DaoEntityNotFoundException and thus the 404 error page is displayed.
     * @param id Entity's id.
     * @return The Entity.
     * @throws DaoEntityNotFoundException If the Entity doesn't exist.
     */
    Entity findById(long id) throws DaoEntityNotFoundException;

    /**
     * Delete an Entity according to it's id.
     * @param id Entity's id.
     */
    void deleteById(long id);

    /**
     * Get an Entity according to it's id.
     * If implemented by a database storage, this method doesn't actually hit the database, it just return a reference of the Role entity.
     * A call to the database is done only if the attributes are accessed.
     * If the Entity doesn't exist Throw an DaoEntityNotFoundException and thus the 404 error page is displayed.
     * @param id Entity's id.
     * @return The Entity.
     * @throws DaoEntityNotFoundException If the Entity doesn't exist.
     */
    Entity getOne(long id) throws DaoEntityNotFoundException;

}
