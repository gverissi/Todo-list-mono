package com.example.todomono.service;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.security.AuthenticationFacadeInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private final CustomerDaoInterface customerDao;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final AuthenticationFacadeInterface authenticationFacade;

    public CustomerService(CustomerDaoInterface customerDao, PasswordEncoder passwordEncoder, AuthenticationFacadeInterface authenticationFacade) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationFacade = authenticationFacade;
    }

    public Customer createCustomer(String name, String password) throws EntityAlreadyExistException {
        if (nameExists(name)) throw new EntityAlreadyExistException("There is already an account with name: " + name + ".");
        String encodedPassword = passwordEncoder.encode(password);
        return customerDao.save(new Customer(name, encodedPassword));
    }

    public Customer getCustomer() {
        String customerName = authenticationFacade.getAuthentication().getName();
        return customerDao.findByName(customerName);
    }

    private boolean nameExists(String name) {
        return customerDao.findByName(name) != null;
    }

}
