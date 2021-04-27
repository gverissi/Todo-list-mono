package com.example.todomono.controller;

import com.example.todomono.entity.Customer;
import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.form.TodoForm;
import com.example.todomono.form.TodoListForm;
import com.example.todomono.service.customer.HomeService;
import com.example.todomono.service.TodoListService;
import com.example.todomono.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = TodoController.class, useDefaultFilters = false)
class TodoControllerUnitTest {

    @MockBean
    private HomeService homeService;

    @MockBean
    private TodoListService todoListService;

    @MockBean
    private TodoService todoService;

    @MockBean
    private BindingResult result;

    @MockBean
    private Model model;

    private TodoController todoController;
    private TodoList todoList;

    private final int TODO_LIST_NUM = 1;

    @BeforeEach
    void init() {
        todoController = new TodoController(homeService, todoListService, todoService);

        Customer customer = mock(Customer.class);
        when(homeService.getCustomer()).thenReturn(customer);
        todoList = mock(TodoList.class);
        when(todoListService.findOneByCustomerAndNum(customer, TODO_LIST_NUM)).thenReturn(todoList);
        TodoListForm todoListForm = mock(TodoListForm.class);
        when(todoList.convertToDto()).thenReturn(todoListForm);
    }

    @Test
    void createATodo() throws EntityAlreadyExistException {
        // Given
        TodoForm todoForm = new TodoForm("my todo");
        // When
        String viewName = todoController.createATodo(TODO_LIST_NUM, todoForm, result, model);
        // Then
        verify(todoService).createOne(todoList, todoForm);
        assertEquals("todo/todo-collection", viewName);
    }

    @Test
    void showAllTodosOfATodoList() {
        // When
        String viewName = todoController.showAllTodosOfATodoList(TODO_LIST_NUM, model);
        // Then
        assertEquals("todo/todo-collection", viewName);
    }

    @Test
    void showOneTodoOfATodoList() {
        // Given
        int todoNum = 1;
        Todo todo = new Todo("my todo");
        when(todoService.getOneByTodoListAndNum(todoList, todoNum)).thenReturn(todo);
        // When
        String viewName = todoController.showOneTodoOfATodoList(TODO_LIST_NUM, todoNum, model);
        // Then
        verify(todoService).getOneByTodoListAndNum(todoList, todoNum);
        assertEquals("todo/todo", viewName);
    }

    @Test
    void updateATodo() throws EntityAlreadyExistException {
        // Given
        int todoNum = 1;
        TodoForm todoForm = new TodoForm("my todo");
        // When
        String viewName = todoController.updateATodo(TODO_LIST_NUM, todoNum, todoForm, result, model);
        // Then
        verify(todoService).updateOneForTodoList(todoList, todoForm);
        assertEquals("redirect:/todo-lists/{todoListNum}/todos", viewName);
    }

    @Test
    void deleteATodo() {
        // Given
        int todoNum = 1;
        // When
        String viewName = todoController.deleteATodo(TODO_LIST_NUM, todoNum);
        // Then
        verify(todoService).deleteOneForTodoList(todoList, todoNum);
        assertEquals("redirect:/todo-lists/{todoListNum}/todos", viewName);
    }

}