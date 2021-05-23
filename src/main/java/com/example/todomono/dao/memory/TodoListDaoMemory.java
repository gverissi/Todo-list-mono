package com.example.todomono.dao.memory;

import com.example.todomono.dao.TodoListDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.DaoConstraintViolationException;
import com.example.todomono.exception.DaoEntityNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Profile("memory")
public class TodoListDaoMemory extends EntityDaoMemory<TodoList> implements TodoListDaoInterface {

    private static long lastId = 0L;

    public TodoListDaoMemory() {
        super();
    }

    @Override
    public TodoList save(TodoList entity) throws DaoConstraintViolationException {
        long entityId = entity.getId();
        String uniqueColumn = entity.getTitle();
        Customer owner = entity.getCustomer();
        List<TodoList> entityByOwner = entityMap.values().stream().filter(e -> e.getCustomer().equals(owner)).collect(Collectors.toList());
        boolean newEntity = false;
        if (!entityMap.containsKey(entityId)) {
            entityId = getNextId();
            newEntity = true;
        } else {
            entityByOwner.remove(entity);
        }
        boolean constraintViolation = entityByOwner.stream().anyMatch(todoList -> todoList.getTitle().equals(uniqueColumn));
        if (constraintViolation) {
            if (newEntity) lastId--;
            throw new DaoConstraintViolationException();
        }
        entity.setId(entityId);
        entityMap.put(entityId, entity);
        return entity;
    }

    @Override
    public List<TodoList> findAllByCustomer(Customer customer) {
        return entityMap.values().stream().filter(todoList -> todoList.getCustomer().equals(customer)).collect(Collectors.toList());
    }

    @Override
    public TodoList findByCustomerAndTitle(Customer customer, String title) {
        return entityMap.values().stream().filter(todoList -> todoList.getCustomer().equals(customer) && todoList.getTitle().equals(title)).findFirst().orElse(null);
    }

    @Override
    public TodoList findByCustomerAndNum(Customer customer, int num) throws DaoEntityNotFoundException {
        TodoList foundTodoList = entityMap.values().stream().filter(todoList -> todoList.getCustomer().equals(customer) && todoList.getNum() == (num)).findFirst().orElse(null);
        if (foundTodoList == null) throw new DaoEntityNotFoundException();
        return foundTodoList;
    }

    @Override
    public int countByCustomer(Customer customer) {
        return findAllByCustomer(customer).size();
    }

    private long getNextId() {
        return ++lastId;
    }

}

