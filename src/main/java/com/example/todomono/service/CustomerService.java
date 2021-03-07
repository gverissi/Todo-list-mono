package com.example.todomono.service;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.exception.CustomerAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private final CustomerDaoInterface customerDao;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerDaoInterface customerDao, PasswordEncoder passwordEncoder) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer createCustomer(String name, String password) {
        if (nameExists(name)) throw new CustomerAlreadyExistException("There is already an account with name: " + name + ".");
        String encodedPassword = passwordEncoder.encode(password);
        return customerDao.save(new Customer(name, encodedPassword));
    }

    private boolean nameExists(String name) {
        return customerDao.findByName(name) != null;
    }

}
