package com.example.todomono.dao.database;

import com.example.todomono.dao.TodoListDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.DaoEntityNotFoundException;
import com.example.todomono.repository.TodoListRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile({"prod", "dev"})
public class TodoListDaoDb extends EntityDaoDb<TodoList, TodoListRepositoryInterface> implements TodoListDaoInterface {

    private final TodoListRepositoryInterface todoListRepository;

    @Autowired
    public TodoListDaoDb(TodoListRepositoryInterface todoListRepository) {
        super(todoListRepository);
        this.todoListRepository = todoListRepository;
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
    public TodoList findByCustomerAndNum(Customer customer, int todoListNum) throws DaoEntityNotFoundException {
        TodoList todoList = todoListRepository.findByCustomerAndNum(customer, todoListNum);
        if (todoList == null) throw new DaoEntityNotFoundException();
        return todoList;
    }

    @Override
    public int countByCustomer(Customer customer) {
        return todoListRepository.countByCustomer(customer);
    }

}

