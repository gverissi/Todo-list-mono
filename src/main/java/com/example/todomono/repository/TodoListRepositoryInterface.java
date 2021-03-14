package com.example.todomono.repository;

import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoListRepositoryInterface extends JpaRepository<TodoList, Integer> {

//    Set<TodoList> findAllByCustomer(Customer customer);
    List<TodoList> findAllByCustomerOrderByNumAsc(Customer customer);

    TodoList findByCustomerAndTitle(Customer customer, String title);

    TodoList findByCustomerAndNum(Customer customer, long todoListNum);

    long countByCustomer(Customer customer);

}
