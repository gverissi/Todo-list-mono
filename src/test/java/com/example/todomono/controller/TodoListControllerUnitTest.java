package com.example.todomono.controller;

import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.TodoListAlreadyExistException;
import com.example.todomono.form.TodoListForm;
import com.example.todomono.service.TodoListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TodoListControllerUnitTest {

    @MockBean
    private TodoListService todoListService;

    @MockBean
    private BindingResult result;

    @MockBean
    private Model model;

    private TodoListController todoListController;

    @BeforeEach
    void init() {
        todoListController = new TodoListController(todoListService);
    }

    @Test
    void createATodoList() throws TodoListAlreadyExistException {
        // Given
        TodoListForm todoListForm = new TodoListForm("my todo-list");
        // When
        todoListController.createATodoList(todoListForm, result, model);
        // Then
        verify(todoListService).createOne(todoListForm);
    }

    @Test
    void showOneTodoListOfACustomer() {
        // Given
        long todoListNum = 1;
        TodoList todoList = new TodoList("my todo-list");
        when(todoListService.getOneByNum(todoListNum)).thenReturn(todoList);
        // When
        todoListController.showOneTodoListOfACustomer(todoListNum, model);
        // Then
        verify(todoListService).getOneByNum(todoListNum);
    }

    @Test
    void updateATodoList() throws TodoListAlreadyExistException {
        // Given
        long todoListNum = 1;
        TodoListForm todoListForm = new TodoListForm("my todo-list");
        // When
        todoListController.updateATodoList(todoListNum, todoListForm, result, model);
        // Then
        verify(todoListService).updateOne(todoListForm);
    }

    @Test
    void deleteATodoList() {
        // Given
        long todoListNum = 1;
        // When
        todoListController.deleteATodoList(todoListNum);
        // Then
        verify(todoListService).deleteOne(todoListNum);
    }
}