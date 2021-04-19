package com.example.todomono.service.customer;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.security.AuthenticationFacadeInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HomeService extends AbstractCustomerService {

    @Autowired
    public HomeService(CustomerDaoInterface customerDao, PasswordEncoder passwordEncoder, AuthenticationFacadeInterface authenticationFacade) {
        super(customerDao, passwordEncoder, authenticationFacade);
    }

    public Customer createCustomer(String name, String password) throws EntityAlreadyExistException {
        if (nameExists(name)) throw new EntityAlreadyExistException("There is already an account with name: " + name + ".");
        String encodedPassword = passwordEncoder.encode(password);
        return customerDao.save(new Customer(name, encodedPassword));
    }

}
