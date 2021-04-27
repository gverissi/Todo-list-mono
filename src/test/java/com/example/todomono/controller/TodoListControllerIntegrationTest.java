package com.example.todomono.controller;

import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;
import com.example.todomono.service.customer.HomeService;
import com.example.todomono.service.TodoListService;
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
class TodoListControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HomeService homeService;

    @MockBean
    private TodoListService todoListService;

    private Customer customer;

    @BeforeEach
    void init() {
        customer = mock(Customer.class);
        when(homeService.getCustomer()).thenReturn(customer);
    }

    @Test
    @WithMockUser(roles = "USER")
    void createATodoList() throws Exception {
        mockMvc.perform(
                post("/todo-lists")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("title", "UTF-8") + "=" + encode("My todo-list", "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("todo-list/todo-list-collection"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void showOneTodoListOfACustomer() throws Exception {
        TodoList todoList = new TodoList("my todo-list");
        int todoListNum = 1;
        when(todoListService.findOneByCustomerAndNum(customer, todoListNum)).thenReturn(todoList);
        mockMvc.perform(
                get("/todo-lists/" + todoListNum))
                .andExpect(status().isOk())
                .andExpect(view().name("todo-list/todo-list"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateATodoList() throws Exception {
        mockMvc.perform(
                put("/todo-lists/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("title", "UTF-8") + "=" + encode("My todo-list", "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/todo-lists"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteATodoList() throws Exception {
        mockMvc.perform(
                delete("/todo-lists/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/todo-lists"));
    }

}