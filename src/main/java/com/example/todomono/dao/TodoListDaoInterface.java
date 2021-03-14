package com.example.todomono.dao;

import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;

import java.util.List;

public interface TodoListDaoInterface {

    TodoList save(TodoList todoList);

    void delete(TodoList todoList);

    TodoList findById(int todoListId);

    List<TodoList> findAllByCustomer(Customer customer);

    TodoList findByCustomerAndTitle(Customer customer, String title);

    TodoList findByCustomerAndNum(Customer customer, long todoListNum);

    long countByCustomer(Customer customer);

}
