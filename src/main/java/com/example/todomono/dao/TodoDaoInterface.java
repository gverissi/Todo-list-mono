package com.example.todomono.dao;

import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;

import java.util.List;

public interface TodoDaoInterface {

    Todo save(Todo todo);

    List<Todo> findAllByTodoList(TodoList todoList);

    Todo findByTodoListAndLabel(TodoList todoList, String label);

    Todo findByTodoListAndNum(TodoList todoList, long num);

    long countByTodoList(TodoList todoList);

}
