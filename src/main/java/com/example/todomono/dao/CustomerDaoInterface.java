package com.example.todomono.dao;

import com.example.todomono.entity.Customer;

public interface CustomerDaoInterface extends EntityDaoInterface<Customer> {

    Customer findByName(String name);

}
