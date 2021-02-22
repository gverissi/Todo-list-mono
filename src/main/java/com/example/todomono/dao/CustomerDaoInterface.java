package com.example.todomono.dao;

import com.example.todomono.entity.Customer;

public interface CustomerDaoInterface {

    void save(Customer customer);

    Customer findByName(String name);

}
