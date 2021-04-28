package com.example.todomono.dao.memory;

import com.example.todomono.dao.TodoDaoInterface;
import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

class TodoDaoMemoryTest {

    TodoDaoInterface todoDao;
    TodoList todoList;
    Todo todo1;
    Todo todo2;

    @BeforeEach
    void init() {
        todoDao = new TodoDaoMemory();
        todoList = new TodoList("My todo-list");
        todo1 = new Todo("label 1");
        todo1.setNum(1);
        todo1.setTodoList(todoList);
        todoDao.save(todo1);
        todo2 = new Todo("label 2");
        todo2.setNum(2);
        todo2.setTodoList(todoList);
        todoDao.save(todo2);
    }

    @Test
    void save() {
        // Given
        String label = "My todo";
        Todo todo = new Todo(label);
        // When
        Todo savedTodoList = todoDao.save(todo);
        // Then
        assertThat(todo.getId(), not(0L));
        assertThat(savedTodoList.getLabel(), equalTo(label));
    }

    @Test
    void findAllByTodoList() {
        // When
        List<Todo> todos = todoDao.findAllByTodoList(todoList);
        // Then
        assertThat(todos.size(), equalTo(2));
    }

    @Test
    void findByTodoListAndLabel() {
        // When
        Todo todo = todoDao.findByTodoListAndLabel(this.todoList, todo2.getLabel());
        // Then
        assertThat(todo, equalTo(todo2));
    }

    @Test
    void findByTodoListAndNum() {
        // When
        Todo todo = todoDao.findByTodoListAndNum(this.todoList, todo2.getNum());
        // Then
        assertThat(todo, equalTo(todo2));
    }

    @Test
    void countByTodoList() {
        // When
        int nbTodos = todoDao.countByTodoList(todoList);
        // Then
        assertThat(nbTodos, equalTo(2));
    }

}