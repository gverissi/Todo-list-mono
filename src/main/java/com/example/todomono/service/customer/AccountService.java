package com.example.todomono.service.customer;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.exception.DaoConstraintViolationException;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.exception.WrongPasswordException;
import com.example.todomono.form.CustomerChangeNameForm;
import com.example.todomono.form.CustomerChangePasswordForm;
import com.example.todomono.form.CustomerDeleteAccountForm;
import com.example.todomono.security.AuthenticationFacadeInterface;
import com.example.todomono.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class AccountService extends AbstractCustomerService {

    @Autowired
    public AccountService(CustomerDaoInterface customerDao, PasswordEncoder passwordEncoder, AuthenticationFacadeInterface authenticationFacade) {
        super(customerDao, passwordEncoder, authenticationFacade);
    }

    public void updateNameOfACustomer(Customer customer, CustomerChangeNameForm customerChangeNameForm) throws WrongPasswordException, EntityAlreadyExistException {
        if (!passwordEncoder.matches(customerChangeNameForm.getPassword(), customer.getEncodedPassword())) throw new WrongPasswordException("Wrong password.");
        customer.setName(customerChangeNameForm.getName());
        Authentication authentication = authenticationFacade.getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        userDetails.setUsername(customer.getName());
        try {
            customerDao.save(customer);
        } catch (DaoConstraintViolationException e) {
            throw new EntityAlreadyExistException("There is already an account with name: " + customerChangeNameForm.getName());
        }
    }

    public void updatePasswordOfACustomer(Customer customer, CustomerChangePasswordForm customerChangePasswordForm) throws WrongPasswordException {
        if (!passwordEncoder.matches(customerChangePasswordForm.getOldPassword(), customer.getEncodedPassword())) throw new WrongPasswordException("Wrong password.");
        String encodedPassword = passwordEncoder.encode(customerChangePasswordForm.getPassword());
        customer.setEncodedPassword(encodedPassword);
        Authentication authentication = authenticationFacade.getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        userDetails.setPassword(customer.getEncodedPassword());
        customerDao.save(customer);
    }

    public void deleteAccount(Customer customer, CustomerDeleteAccountForm customerDeleteAccountForm, HttpSession session) throws WrongPasswordException {
        if (!passwordEncoder.matches(customerDeleteAccountForm.getPassword(), customer.getEncodedPassword())) throw new WrongPasswordException("Wrong password.");
        session.invalidate();
        SecurityContextHolder.clearContext();
        customerDao.deleteById(customer.getId());
    }

}
