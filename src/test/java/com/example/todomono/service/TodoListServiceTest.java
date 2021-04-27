package com.example.todomono.service;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.dao.TodoListDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.form.TodoListForm;
import com.example.todomono.security.AuthenticationFacadeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class TodoListServiceTest {

    String customerName = "greg";
    String encodedPassword = "AbCd";
    String todoListTitle = "My todo-list";

    Customer customer = new Customer(customerName, encodedPassword);

    TodoListDaoInterface todoListDaoMock = mock(TodoListDaoInterface.class);
    CustomerDaoInterface customerDaoMock = mock(CustomerDaoInterface.class);
    AuthenticationFacadeInterface authenticationFacadeMock = mock(AuthenticationFacadeInterface.class);
    Authentication authenticationMock = mock(Authentication.class);

    TodoListService todoListService = new TodoListService(todoListDaoMock);

    @BeforeEach
    void init() {
        when(todoListDaoMock.save(any(TodoList.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getName()).thenReturn(customerName);
        when(customerDaoMock.findByName(customerName)).thenReturn(customer);
    }

    @Test
    void createOne() throws EntityAlreadyExistException {
        // Given
        when(todoListDaoMock.findByCustomerAndTitle(customer, todoListTitle)).thenReturn(null);
        TodoListForm todoListForm = new TodoListForm(todoListTitle);
        // When
        TodoList todoList = todoListService.createOneForCustomer(customer, todoListForm);
        // Then
        assertEquals(todoListForm.getTitle(), todoList.getTitle());
        assertEquals(customer, todoList.getCustomer());
        assertEquals(1, todoList.getNum());
        verify(todoListDaoMock).save(todoList);
        verify(todoListDaoMock).countByCustomer(customer);
    }

    @Test
    void getOneByNum() {
        // Given
        int todoListNum = 1;
        TodoList todoList = new TodoList(todoListTitle);
        todoList.setNum(todoListNum);
        when(todoListDaoMock.findByCustomerAndNum(customer, todoListNum)).thenReturn(todoList);
        // When
        TodoList foundTodoList = todoListService.findOneByCustomerAndNum(customer, todoListNum);
        // Then
        assertEquals(foundTodoList.getTitle(), todoList.getTitle());
        assertEquals(foundTodoList.getNum(), todoList.getNum());
        verify(todoListDaoMock).findByCustomerAndNum (customer, todoListNum);
    }

    @Test
    void updateOne() throws EntityAlreadyExistException {
        // Given
        int todoListNum = 1;
        TodoList todoList = new TodoList(todoListTitle);
        todoList.setNum(todoListNum);
        String newTitle = "new title";
        TodoListForm todoListForm = new TodoListForm(newTitle);
        todoListForm.setNum(todoListNum);
        when(todoListDaoMock.findByCustomerAndTitle(customer, newTitle)).thenReturn(null);
        when(todoListDaoMock.findByCustomerAndNum(customer, todoListNum)).thenReturn(todoList);
        // When
        TodoList updatedTodoList = todoListService.updateOneForCustomer(customer, todoListForm);
        // Then
        assertEquals(newTitle, updatedTodoList.getTitle());
        assertEquals(todoListNum, updatedTodoList.getNum());
        verify(todoListDaoMock).findByCustomerAndNum (customer, todoListNum);
    }

    @Test
    void deleteOne() {
        // Given
        TodoList todoList1 = new TodoList("todo-list-1");
        todoList1.setId(1);
        todoList1.setNum(1);
        TodoList todoList2 = new TodoList("todo-list-2");
        todoList2.setId(2);
        todoList2.setNum(2);
        TodoList todoList3 = new TodoList("todo-list-3");
        todoList3.setId(3);
        todoList3.setNum(3);
        List<TodoList> todoLists = new ArrayList<>(Arrays.asList(todoList1, todoList2, todoList3));
        when(todoListDaoMock.findByCustomerAndNum(customer, todoList2.getNum())).thenReturn(todoList2);
        when(todoListDaoMock.findAllByCustomer(customer)).thenReturn(todoLists);
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            assertEquals(todoList2.getId(), arg0);
            todoLists.remove(todoList2);
            return null;
        }).when(todoListDaoMock).deleteById(todoList2.getId());
        // When
        todoListService.deleteOneForCustomer(customer, todoList2.getNum());
        // Then
        assertThat(todoLists, hasSize(2));
        assertEquals(1, todoList1.getNum());
        assertEquals(2, todoList3.getNum());
        verify(todoListDaoMock).findByCustomerAndNum(customer, todoList2.getNum());
        verify(todoListDaoMock).save(todoList1);
        verify(todoListDaoMock).save(todoList3);
    }

}