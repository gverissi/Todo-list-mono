package com.example.todomono.repository;

import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepositoryInterface extends JpaRepository<Todo, Long> {

    List<Todo> findAllByTodoListOrderByNumAsc(TodoList todoList);

    Todo findByTodoListAndLabel(TodoList todoList, String label);

    Todo findByTodoListAndNum(TodoList todoList, int num);

    int countByTodoList(TodoList todoList);

}
