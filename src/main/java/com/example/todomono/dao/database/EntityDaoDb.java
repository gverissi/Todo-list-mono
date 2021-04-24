package com.example.todomono.dao.database;

import com.example.todomono.dao.EntityDaoInterface;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class EntityDaoDb<Entity, Repository extends JpaRepository<Entity, Long>> implements EntityDaoInterface<Entity> {

    private final Repository repository;

    public EntityDaoDb(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Entity save(Entity entity) {
        return repository.save(entity);
    }

    @Override
    public List<Entity> findAll() {
        return repository.findAll();
    }

    @Override
    public Entity findById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    public Entity getOne(long id) {
        return repository.getOne(id);
    }

}
