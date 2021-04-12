package com.example.todomono.controller;

import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.TodoListAlreadyExistException;
import com.example.todomono.form.TodoListForm;
import com.example.todomono.service.CustomerService;
import com.example.todomono.service.TodoListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class TodoListControllerUnitTest {

    @MockBean
    private CustomerService customerService;

    @MockBean
    private TodoListService todoListService;

    @MockBean
    private BindingResult result;

    @MockBean
    private Model model;

    private TodoListController todoListController;
    private Customer customer;

    @BeforeEach
    void init() {
        todoListController = new TodoListController(customerService, todoListService);
        customer = mock(Customer.class);
        when(customerService.getCustomer()).thenReturn(customer);
    }

    @Test
    void createATodoList() throws TodoListAlreadyExistException {
        // Given
        TodoListForm todoListForm = new TodoListForm("my todo-list");
        // When
        String viewName = todoListController.createATodoList(todoListForm, result, model);
        // Then
        verify(todoListService).createOneForCustomer(customer, todoListForm);
        assertEquals("todo-list-collection", viewName);
    }

    @Test
    void showAllTodoListsOfACustomer() {
        // When
        String viewName = todoListController.showAllTodoListsOfACustomer(model);
        // Then
        assertEquals("todo-list-collection", viewName);
    }

    @Test
    void showOneTodoListOfACustomer() {
        // Given
        long todoListNum = 1;
        TodoList todoList = new TodoList("my todo-list");
        when(todoListService.getOneByCustomerAndNum(customer, todoListNum)).thenReturn(todoList);
        // When
        String viewName = todoListController.showOneTodoListOfACustomer(todoListNum, model);
        // Then
        verify(todoListService).getOneByCustomerAndNum(customer, todoListNum);
        assertEquals("todo-list", viewName);
    }

    @Test
    void updateATodoList() throws TodoListAlreadyExistException {
        // Given
        long todoListNum = 1;
        TodoListForm todoListForm = new TodoListForm("my todo-list");
        // When
        String viewName = todoListController.updateATodoList(todoListNum, todoListForm, result, model);
        // Then
        verify(todoListService).updateOneForCustomer(customer, todoListForm);
        assertEquals("redirect:/todo-lists", viewName);
    }

    @Test
    void deleteATodoList() {
        // Given
        long todoListNum = 1;
        // When
        String viewName = todoListController.deleteATodoList(todoListNum);
        // Then
        verify(todoListService).deleteOneForCustomer(customer, todoListNum);
        assertEquals("redirect:/todo-lists", viewName);
    }
}