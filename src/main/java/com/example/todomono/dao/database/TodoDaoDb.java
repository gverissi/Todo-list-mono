package com.example.todomono.dao.database;

import com.example.todomono.dao.TodoDaoInterface;
import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.repository.TodoRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TodoDaoDb extends EntityDaoDb<Todo, TodoRepositoryInterface> implements TodoDaoInterface {

    private final TodoRepositoryInterface todoRepository;

    @Autowired
    public TodoDaoDb(TodoRepositoryInterface todoRepository) {
        super(todoRepository);
        this.todoRepository = todoRepository;
    }

    @Override
    public List<Todo> findAllByTodoList(TodoList todoList) {
        return todoRepository.findAllByTodoListOrderByNumAsc(todoList);
    }

    @Override
    public Todo findByTodoListAndLabel(TodoList todoList, String label) {
        return todoRepository.findByTodoListAndLabel(todoList, label);
    }

    @Override
    public Todo findByTodoListAndNum(TodoList todoList, long num) {
        return todoRepository.findByTodoListAndNum(todoList, num);
    }

    @Override
    public long countByTodoList(TodoList todoList) {
        return todoRepository.countByTodoList(todoList);
    }

}
