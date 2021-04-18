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
public class AccountService extends AbstractCustomerService {

    @Autowired
    public AccountService(CustomerDaoInterface customerDao, PasswordEncoder passwordEncoder, AuthenticationFacadeInterface authenticationFacade) {
        super(customerDao, passwordEncoder, authenticationFacade);
    }

    public void updateNameOfACustomer(Customer customer, ChangeCustomerNameForm changeCustomerNameForm) throws WrongPasswordException, EntityAlreadyExistException {
        if (!passwordEncoder.matches(changeCustomerNameForm.getPassword(), customer.getPassword())) throw new WrongPasswordException("Wrong password.");
        if (nameExists(changeCustomerNameForm.getName())) throw new EntityAlreadyExistException("There is already an account with name: " + changeCustomerNameForm.getName() + ".");
        customer.setName(changeCustomerNameForm.getName());
        Authentication authentication = authenticationFacade.getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        userDetails.setUsername(customer.getName());
        customerDao.save(customer);
    }

}
