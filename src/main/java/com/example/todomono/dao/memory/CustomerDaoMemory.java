package com.example.todomono.dao.memory;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;

import java.util.HashMap;
import java.util.Map;

public class CustomerDaoMemory implements CustomerDaoInterface {

    private final Map<String, Customer> customerMap = new HashMap<>();

    @Override
    public void save(Customer customer) {
        customerMap.put(customer.getName(), customer);
    }

    @Override
    public Customer findByName(String name) {
        Customer customer = customerMap.get(name);
        if (customer == null) throw new RuntimeException(name);
        return customer;
    }

}