package com.example.todomono.service;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.exception.WrongPasswordException;
import com.example.todomono.form.ChangeCustomerNameForm;
import com.example.todomono.security.AuthenticationFacadeInterface;
import com.example.todomono.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AbstractCustomerService {

    protected final CustomerDaoInterface customerDao;

    protected final PasswordEncoder passwordEncoder;

    protected final AuthenticationFacadeInterface authenticationFacade;

    public AbstractCustomerService(CustomerDaoInterface customerDao, PasswordEncoder passwordEncoder, AuthenticationFacadeInterface authenticationFacade) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationFacade = authenticationFacade;
    }

    protected boolean nameExists(String name) {
        return customerDao.findByName(name) != null;
    }

}
