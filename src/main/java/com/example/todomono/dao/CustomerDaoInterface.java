package com.example.todomono.dao;

import com.example.todomono.entity.Customer;
import com.example.todomono.exception.DaoEntityNotFoundException;

public interface CustomerDaoInterface extends EntityDaoInterface<Customer> {

    Customer findByName(String name) throws DaoEntityNotFoundException;

}
