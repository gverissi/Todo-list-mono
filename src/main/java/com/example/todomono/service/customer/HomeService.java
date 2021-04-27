package com.example.todomono.service.customer;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.Role;
import com.example.todomono.exception.DaoConstraintViolationException;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.form.CustomerCreateForm;
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

    public Customer createCustomer(CustomerCreateForm customerCreateForm, Role role) throws EntityAlreadyExistException {
        String name = customerCreateForm.getName();
        String encodedPassword = passwordEncoder.encode(customerCreateForm.getPassword());
        Customer customer = new Customer(name, encodedPassword);
        customer.addRole(role);
        try {
            return customerDao.save(customer);
        } catch (DaoConstraintViolationException e) {
            throw new EntityAlreadyExistException("There is already an account with name: " + name);
        }
    }

}
