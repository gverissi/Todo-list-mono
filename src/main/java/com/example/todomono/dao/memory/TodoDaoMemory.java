package com.example.todomono.dao.memory;

import com.example.todomono.dao.TodoDaoInterface;
import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.DaoConstraintViolationException;
import com.example.todomono.exception.DaoEntityNotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Profile("memory")
public class TodoDaoMemory extends EntityDaoMemory<Todo> implements TodoDaoInterface {

    private static long lastId = 0L;

    public TodoDaoMemory() {
        super();
    }

    @Override
    public Todo save(Todo entity) throws DaoConstraintViolationException {
        long entityId = entity.getId();
        String uniqueColumn = entity.getLabel();
        TodoList owner = entity.getTodoList();
        List<Todo> entityByOwner = entityMap.values().stream().filter(e -> e.getTodoList().equals(owner)).collect(Collectors.toList());
        boolean newEntity = false;
        if (!entityMap.containsKey(entityId)) {
            entityId = getNextId();
            newEntity = true;
        } else {
            entityByOwner.remove(entity);
        }
        boolean constraintViolation = entityByOwner.stream().anyMatch(todoList -> todoList.getLabel().equals(uniqueColumn));
        if (constraintViolation) {
            if (newEntity) lastId--;
            throw new DaoConstraintViolationException();
        }
        entity.setId(entityId);
        entityMap.put(entityId, entity);
        return entity;
    }

    @Override
    public List<Todo> findAllByTodoList(TodoList todoList) {
        return entityMap.values().stream().filter(todo -> todo.getTodoList().equals(todoList)).collect(Collectors.toList());
    }

    @Override
    public Todo findByTodoListAndLabel(TodoList todoList, String label) {
        return entityMap.values().stream().filter(todo -> todo.getTodoList().equals(todoList) && todo.getLabel().equals(label)).findFirst().orElse(null);
    }

    @Override
    public Todo findByTodoListAndNum(TodoList todoList, int num) throws DaoEntityNotFoundException {
        Todo foundTodo = entityMap.values().stream().filter(todo -> todo.getTodoList().equals(todoList) && todo.getNum() == (num)).findFirst().orElse(null);
        if (foundTodo == null) throw new DaoEntityNotFoundException();
        return foundTodo;
    }

    @Override
    public int countByTodoList(TodoList todoList) {
        return findAllByTodoList(todoList).size();
    }

    private long getNextId() {
        return ++lastId;
    }

}
