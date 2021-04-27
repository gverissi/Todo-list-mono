package com.example.todomono.dao.database;

import com.example.todomono.dao.EntityDaoInterface;
import com.example.todomono.exception.DaoConstraintViolationException;
import com.example.todomono.exception.DaoEntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public abstract class EntityDaoDb<Entity, Repository extends JpaRepository<Entity, Long>> implements EntityDaoInterface<Entity> {

    private final Repository repository;

    public EntityDaoDb(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Entity save(Entity entity) throws DaoConstraintViolationException {
        Entity savedEntity = null;
        try {
            savedEntity = repository.save(entity);
        } catch (DataAccessException e) {
            if (e.getCause().getClass().getSimpleName().equals("ConstraintViolationException")) {
                throw new DaoConstraintViolationException();
            }
        }
        return savedEntity;
    }

    @Override
    public List<Entity> findAll() {
        return repository.findAll();
    }

    @Override
    public Entity findById(long id) throws DaoEntityNotFoundException {
        Entity entity = repository.findById(id).orElse(null);
        if (entity == null) throw new DaoEntityNotFoundException();
        return entity;
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    public Entity getOne(long id) throws DaoEntityNotFoundException {
        try {
            return repository.getOne(id);
        } catch (EntityNotFoundException e) {
            throw new DaoEntityNotFoundException();
        }
    }

}
