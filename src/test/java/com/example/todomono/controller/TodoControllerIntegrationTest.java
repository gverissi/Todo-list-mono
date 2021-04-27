package com.example.todomono.controller;

import com.example.todomono.entity.Customer;
import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.form.TodoListForm;
import com.example.todomono.service.TodoListService;
import com.example.todomono.service.TodoService;
import com.example.todomono.service.customer.HomeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.web.util.UriUtils.encode;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HomeService homeService;

    @MockBean
    private TodoListService todoListService;

    @MockBean
    private TodoService todoService;

    private TodoList todoList;

    private final int TODO_LIST_NUM = 1;

    @BeforeEach
    void init() {
        Customer customer = mock(Customer.class);
        when(homeService.getCustomer()).thenReturn(customer);
        todoList = mock(TodoList.class);
        when(todoListService.findOneByCustomerAndNum(customer, TODO_LIST_NUM)).thenReturn(todoList);
        when(todoList.convertToDto()).thenReturn(mock(TodoListForm.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createATodo() throws Exception {
        mockMvc.perform(
                post("/todo-lists/" + TODO_LIST_NUM + "/todos")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("label", "UTF-8") + "=" + encode("My todo", "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("todo/todo-collection"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void showOneTodoOfATodoList() throws Exception {
        Todo todo = new Todo("My todo");
        todo.setNum(1);
        when(todoService.getOneByTodoListAndNum(todoList, todo.getNum())).thenReturn(todo);
        mockMvc.perform(
                get("/todo-lists/" + TODO_LIST_NUM + "/todos/" + todo.getNum()))
                .andExpect(status().isOk())
                .andExpect(view().name("todo/todo"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateATodo() throws Exception {
        mockMvc.perform(
                put("/todo-lists/" + TODO_LIST_NUM + "/todos/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("label", "UTF-8") + "=" + encode("My todo-list", "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/todo-lists/{todoListNum}/todos"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteATodo() throws Exception {
        mockMvc.perform(
                delete("/todo-lists/" + TODO_LIST_NUM + "/todos/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/todo-lists/{todoListNum}/todos"));
    }

}