package com.example.todomono.dao.memory;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.exception.DaoConstraintViolationException;
import com.example.todomono.exception.DaoEntityNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("memory")
public class CustomerDaoMemory extends EntityDaoMemory<Customer> implements CustomerDaoInterface {

    private static long lastId = 0;

    public CustomerDaoMemory() {
        super();
    }

    @Override
    public Customer save(Customer entity) throws DaoConstraintViolationException {
        long entityId = entity.getId();
        String uniqueColumn = entity.getName();
        List<Customer> entities = findAll();
        boolean newEntity = false;
        if (!entityMap.containsKey(entityId)) {
            entityId = getNextId();
            newEntity = true;
        } else {
            entities.remove(entity);
        }
        boolean constraintViolation = entities.stream().anyMatch(todoList -> todoList.getName().equals(uniqueColumn));
        if (constraintViolation) {
            if (newEntity) lastId--;
            throw new DaoConstraintViolationException();
        }
        entity.setId(entityId);
        entityMap.put(entityId, entity);
        return entity;
    }

    @Override
    public Customer findByName(String name) throws DaoEntityNotFoundException {
        Customer foundCustomer = entityMap.values().stream().filter(customer -> customer.getName().equals(name)).findFirst().orElse(null);
        if (foundCustomer == null) throw new DaoEntityNotFoundException();
        return foundCustomer;
    }

    private long getNextId() {
        return ++lastId;
    }

}
