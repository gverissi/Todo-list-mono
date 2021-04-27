package com.example.todomono.dao;

import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.DaoEntityNotFoundException;

import java.util.List;

public interface TodoDaoInterface extends EntityDaoInterface<Todo> {

    List<Todo> findAllByTodoList(TodoList todoList);

    Todo findByTodoListAndLabel(TodoList todoList, String label);

    Todo findByTodoListAndNum(TodoList todoList, int num) throws DaoEntityNotFoundException;

    int countByTodoList(TodoList todoList);

}
