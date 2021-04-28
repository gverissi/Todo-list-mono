package com.example.todomono.dao;

import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.DaoEntityNotFoundException;

import java.util.List;

public interface TodoListDaoInterface extends EntityDaoInterface<TodoList> {

    List<TodoList> findAllByCustomer(Customer customer);

    TodoList findByCustomerAndTitle(Customer customer, String title);

    TodoList findByCustomerAndNum(Customer customer, int num) throws DaoEntityNotFoundException;

    int countByCustomer(Customer customer);

}
