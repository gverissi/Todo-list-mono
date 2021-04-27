package com.example.todomono.dao.memory;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.exception.DaoConstraintViolationException;
import com.example.todomono.exception.DaoEntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDaoMemory implements CustomerDaoInterface {

    private final Map<String, Customer> customerMap = new HashMap<>();

    @Override
    public Customer save(Customer customer) throws DaoConstraintViolationException {
        customerMap.put(customer.getName(), customer);
        return customer;
    }

    @Override
    public Customer findByName(String name) throws DaoEntityNotFoundException {
        Customer customer = customerMap.get(name);
        if (customer == null) throw new DaoEntityNotFoundException();
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
    public Customer getOne(long id) throws DaoEntityNotFoundException {
        return null;
    }

    @Override
    public Customer findById(long customerId) throws DaoEntityNotFoundException {
        return null;
    }

}
