package com.example.todomono.controller;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.dao.TodoDaoInterface;
import com.example.todomono.dao.TodoListDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
class TodoControllerFctTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoDaoInterface todoDao;

    @Autowired
    private TodoListDaoInterface todoListDao;

    @Autowired
    private CustomerDaoInterface customerDao;

    private TodoList todoList;

    @BeforeAll
    @WithMockUser(roles = "USER", username = "greg")
    void init() {
        Customer customer = customerDao.findByName("greg");
        todoList = new TodoList("My todo-list");
        todoList.setNum(1);
        todoList.setCustomer(customer);
        todoListDao.save(todoList);

        Todo todo = new Todo("My todo 1");
        todo.setNum(1);
        todo.setTodoList(todoList);
        todoDao.save(todo);
        todo = new Todo("My todo 2");
        todo.setNum(2);
        todo.setTodoList(todoList);
        todoDao.save(todo);
        todo = new Todo("My todo 3");
        todo.setNum(3);
        todo.setTodoList(todoList);
        todoDao.save(todo);
    }

    @Test
    @WithMockUser(roles = "USER", username = "greg")
    void createATodo() throws Exception {
        String label = "My todo";
        int nbTodos = todoDao.countByTodoList(todoList);

        mockMvc.perform(
                post("/todo-lists/" + todoList.getNum() + "/todos")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("label", "UTF-8") + "=" + encode(label, "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("todo/todo-collection"));

        Todo todo = todoDao.findByTodoListAndLabel(todoList, label);
        assertEquals(label, todo.getLabel());
        List<Todo> todos = todoDao.findAllByTodoList(todoList);
        assertEquals(nbTodos + 1, todos.size());
    }

    @Test
    @WithMockUser(roles = "USER", username = "greg")
    void updateATodo() throws Exception {
        String newLabel = "My new title";
        mockMvc.perform(
                put("/todo-lists/" + todoList.getNum() + "/todos/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("label", "UTF-8") + "=" + encode(newLabel, "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/todo-lists/{todoListNum}/todos"));

        Todo updatedTodo = todoDao.findByTodoListAndLabel(todoList, newLabel);
        assertEquals(newLabel, updatedTodo.getLabel());
    }

    @Test
    @WithMockUser(roles = "USER", username = "greg")
    void deleteATodo() throws Exception {
        int nbTodos = todoDao.countByTodoList(todoList);

        mockMvc.perform(
                delete("/todo-lists/" + todoList.getNum() + "/todos/2")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/todo-lists/{todoListNum}/todos"));

        List<Todo> todos = todoDao.findAllByTodoList(todoList);
        assertEquals(nbTodos - 1, todos.size());
    }

}