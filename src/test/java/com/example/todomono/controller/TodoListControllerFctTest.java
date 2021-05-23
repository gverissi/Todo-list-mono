package com.example.todomono.controller;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.dao.TodoListDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.web.util.UriUtils.encode;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
class TodoListControllerFctTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoListDaoInterface todoListDao;

    @Autowired
    private CustomerDaoInterface customerDao;

    private Customer customer;

    @BeforeAll
    @WithMockUser(roles = "USER", username = "greg")
    void init() {
        customer = customerDao.findByName("greg");
        TodoList todoList = new TodoList("My todo-list 1");
        todoList.setNum(1);
        todoList.setCustomer(customer);
        todoListDao.save(todoList);
        todoList = new TodoList("My todo-list 2");
        todoList.setNum(2);
        todoList.setCustomer(customer);
        todoListDao.save(todoList);
        todoList = new TodoList("My todo-list 3");
        todoList.setNum(3);
        todoList.setCustomer(customer);
        todoListDao.save(todoList);
    }

    @Test
    @WithMockUser(roles = "USER", username = "greg")
    void createATodoList() throws Exception {
        String title = "My todo-list";
        int nbTodoLists = todoListDao.countByCustomer(customer);

        mockMvc.perform(
                post("/todo-lists")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("title", "UTF-8") + "=" + encode(title, "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("todo-list/todo-list-collection"));

        TodoList todoList = todoListDao.findByCustomerAndTitle(customer, title);
        assertEquals(title, todoList.getTitle());
        List<TodoList> todoLists = todoListDao.findAllByCustomer(customer);
        assertEquals(nbTodoLists + 1, todoLists.size());
    }

    @Test
    @WithMockUser(roles = "USER", username = "greg")
    void updateATodoList() throws Exception {
        String newTitle = "My new title";

        mockMvc.perform(
                put("/todo-lists/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("title", "UTF-8") + "=" + encode(newTitle, "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/todo-lists"));

        TodoList updatedTodoList = todoListDao.findByCustomerAndTitle(customer, newTitle);
        assertEquals(newTitle, updatedTodoList.getTitle());
    }

    @Test
    @WithMockUser(roles = "USER", username = "greg")
    void deleteATodoList() throws Exception {
        int nbTodoLists = todoListDao.countByCustomer(customer);

        mockMvc.perform(
                delete("/todo-lists/2")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/todo-lists"));

        List<TodoList> todoLists = todoListDao.findAllByCustomer(customer);
        assertEquals(nbTodoLists - 1, todoLists.size());
    }

}