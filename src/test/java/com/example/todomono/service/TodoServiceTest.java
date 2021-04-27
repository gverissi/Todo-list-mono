package com.example.todomono.service;

import com.example.todomono.dao.TodoDaoInterface;
import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.form.TodoForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    private final TodoList todoListMock = mock(TodoList.class);
    private final TodoDaoInterface todoDaoMock = mock(TodoDaoInterface.class);

    private final String todoLabel = "My todo";
    private final TodoService todoService = new TodoService(todoDaoMock);

    @BeforeEach
    void init() {
        when(todoDaoMock.save(any(Todo.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
    }

    @Test
    void createOne() throws EntityAlreadyExistException {
        // Given
        when(todoDaoMock.findByTodoListAndLabel(todoListMock, todoLabel)).thenReturn(null);
        TodoForm todoForm = new TodoForm(todoLabel);
        // When
        Todo createdTodo = todoService.createOne(todoListMock, todoForm);
        // Then
        assertEquals(todoForm.getLabel(), createdTodo.getLabel());
        assertEquals(todoListMock, createdTodo.getTodoList());
        assertEquals(1, createdTodo.getNum());
        verify(todoDaoMock).save(createdTodo);
        verify(todoDaoMock).countByTodoList(todoListMock);
    }

    @Test
    void findAllByTodoList() {
        // When
        todoService.findAllByTodoList(todoListMock);
        // Then
        verify(todoDaoMock).findAllByTodoList(todoListMock);
    }

    @Test
    void getOneByTodoListAndNum() {
        // Given
        int todoNum = 1;
        Todo todo = new Todo(todoLabel);
        todo.setNum(todoNum);
        when(todoDaoMock.findByTodoListAndNum(todoListMock, todoNum)).thenReturn(todo);
        // When
        Todo foundTodo = todoService.getOneByTodoListAndNum(todoListMock, todoNum);
        // Then
        assertEquals(todo.getLabel(), foundTodo.getLabel());
        assertEquals(todo.getNum(), foundTodo.getNum());
        verify(todoDaoMock).findByTodoListAndNum(todoListMock, todoNum);
    }

    @Test
    void updateOneForTodoList() throws EntityAlreadyExistException {
        // Given
        int todoNum = 1;
        Todo todo = new Todo(todoLabel);
        todo.setNum(todoNum);
        String newLabel = "new label";
        TodoForm todoForm = new TodoForm(newLabel);
        todoForm.setNum(todoNum);
        when(todoDaoMock.findByTodoListAndLabel(todoListMock, newLabel)).thenReturn(null);
        when(todoDaoMock.findByTodoListAndNum(todoListMock, todoNum)).thenReturn(todo);
        // When
        Todo updatedTodo = todoService.updateOneForTodoList(todoListMock, todoForm);
        // Then
        assertEquals(newLabel, updatedTodo.getLabel());
        assertEquals(todoNum, updatedTodo.getNum());
        verify(todoDaoMock).findByTodoListAndNum(todoListMock, todoNum);
    }

    @Test
    void deleteOneForTodoList() {
        // Given
        Todo todo1 = new Todo("todo-1");
        todo1.setId(1);
        todo1.setNum(1);
        Todo todo2 = new Todo("todo-2");
        todo2.setId(2);
        todo2.setNum(2);
        Todo todo3 = new Todo("todo-3");
        todo3.setId(3);
        todo3.setNum(2);
        List<Todo> todos = new ArrayList<>(Arrays.asList(todo1, todo2, todo3));
        when(todoDaoMock.findByTodoListAndNum(todoListMock, todo2.getNum())).thenReturn(todo2);
        when(todoDaoMock.findAllByTodoList(todoListMock)).thenReturn(todos);
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            assertEquals(todo2.getId(), arg0);
            todos.remove(todo2);
            return null;
        }).when(todoDaoMock).deleteById(todo2.getId());
        // When
        todoService.deleteOneForTodoList(todoListMock, todo2.getNum());
        // Then
        assertThat(todos, hasSize(2));
        assertEquals(1, todo1.getNum());
        assertEquals(2, todo3.getNum());
        verify(todoDaoMock).findByTodoListAndNum(todoListMock, todo2.getNum());
        verify(todoDaoMock).save(todo1);
        verify(todoDaoMock).save(todo3);
    }

}