package com.example.todomono.dao.database;

import com.example.todomono.dao.TodoListDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;
import com.example.todomono.repository.TodoListRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TodoListDaoDb implements TodoListDaoInterface {

    @Autowired
    private final TodoListRepositoryInterface todoListRepository;

    public TodoListDaoDb(TodoListRepositoryInterface todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

    @Override
    public TodoList save(TodoList todoList) {
        return todoListRepository.save(todoList);
    }

    @Override
    public void delete(TodoList todoList) {
        todoListRepository.delete(todoList);
    }

    @Override
    public TodoList findById(int todoListId) {
        return todoListRepository.findById(todoListId).orElse(null);
    }

    @Override
    public List<TodoList> findAllByCustomer(Customer customer) {
        return todoListRepository.findAllByCustomerOrderByNumAsc(customer);
    }

    @Override
    public TodoList findByCustomerAndTitle(Customer customer, String title) {
        return todoListRepository.findByCustomerAndTitle(customer, title);
    }

    @Override
    public TodoList findByCustomerAndNum(Customer customer, long todoListNum) {
        return todoListRepository.findByCustomerAndNum(customer, todoListNum);
    }

    @Override
    public long countByCustomer(Customer customer) {
        return todoListRepository.countByCustomer(customer);
    }

}
