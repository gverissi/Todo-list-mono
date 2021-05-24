package com.example.todomono.dao;

import com.example.todomono.entity.Customer;
import com.example.todomono.exception.DaoEntityNotFoundException;

public interface CustomerDaoInterface extends EntityDaoInterface<Customer> {

    /**
     * Find a Customer according to he's name.
     * If the Customer doesn't exist Throw an DaoEntityNotFoundException and thus the 404 error page is displayed.
     * @param name Customer's name.
     * @return The Cutomer entity.
     * @throws DaoEntityNotFoundException If the Customer doesn't exist.
     */
    Customer findByName(String name) throws DaoEntityNotFoundException;

}
