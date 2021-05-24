package com.example.todomono.dao;

import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.DaoEntityNotFoundException;

import java.util.List;

public interface TodoListDaoInterface extends EntityDaoInterface<TodoList> {

    /**
     * Find all TodoLists of a Customer.
     * @param customer Parent of the TodoLists.
     * @return List of TodoList.
     */
    List<TodoList> findAllByCustomer(Customer customer);

    /**
     * Find a TodoList according to it's title.
     * @param customer Parent od the TodoList.
     * @param title Title of the TodoList.
     * @return The TodoList entity.
     */
    TodoList findByCustomerAndTitle(Customer customer, String title);

    /**
     * Find a TodoList according to it's num.
     * If the TodoList doesn't exist Throw an DaoEntityNotFoundException and thus the 404 error page is displayed.
     * @param customer Parent od the TodoList.
     * @param num Num of the TodoList.
     * @return The TodoList entity.
     * @throws DaoEntityNotFoundException If the TodoList doesn't exist.
     */
    TodoList findByCustomerAndNum(Customer customer, int num) throws DaoEntityNotFoundException;

    /**
     * Count all TodoLists of a Customer.
     * @param customer Parent od the TodoLists.
     * @return Number of TodoLists of a Customer.
     */
    int countByCustomer(Customer customer);

}
