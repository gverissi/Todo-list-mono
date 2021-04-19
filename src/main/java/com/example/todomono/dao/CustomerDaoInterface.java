package com.example.todomono.dao;

import com.example.todomono.entity.Customer;

import java.util.List;

public interface CustomerDaoInterface {

    Customer save(Customer customer);

    Customer findByName(String name);

    List<Customer> findAll();

    void deleteById(int customerId);

}
