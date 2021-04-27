package com.example.todomono.service.customer;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.security.AuthenticationFacadeInterface;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class AbstractCustomerService {

    protected final CustomerDaoInterface customerDao;

    protected final PasswordEncoder passwordEncoder;

    protected final AuthenticationFacadeInterface authenticationFacade;

    public AbstractCustomerService(CustomerDaoInterface customerDao, PasswordEncoder passwordEncoder, AuthenticationFacadeInterface authenticationFacade) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationFacade = authenticationFacade;
    }

    public Customer getCustomer() {
        String customerName = authenticationFacade.getAuthentication().getName();
        return customerDao.findByName(customerName);
    }

}
