package com.example.todomono.dao.memory;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDaoMemory implements CustomerDaoInterface {

    private final Map<String, Customer> customerMap = new HashMap<>();

    @Override
    public Customer save(Customer customer) {
        customerMap.put(customer.getName(), customer);
        return customer;
    }

    @Override
    public Customer findByName(String name) {
        Customer customer = customerMap.get(name);
        if (customer == null) throw new RuntimeException(name);
        return customer;
    }

    @Override
    public List<Customer> findAll() {
        return null;
    }

    @Override
    public void deleteById(long customerId) {

    }

    @Override
    public Customer getOne(long id) {
        return null;
    }

    @Override
    public Customer findById(long customerId) {
        return null;
    }

}
