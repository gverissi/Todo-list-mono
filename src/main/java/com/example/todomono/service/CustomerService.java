package com.example.todomono.service;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private final CustomerDaoInterface customerDao;

    public CustomerService(CustomerDaoInterface customerDao) {
        this.customerDao = customerDao;
    }

    public Customer createCustomer(String name, String password) {
        Customer customer = new Customer(name, password);
        customerDao.save(customer);
        return customer;
    }

}
