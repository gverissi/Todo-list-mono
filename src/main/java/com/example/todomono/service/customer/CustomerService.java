package com.example.todomono.service.customer;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.security.AuthenticationFacadeInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class CustomerService extends AbstractCustomerService {

    @Autowired
    public CustomerService(CustomerDaoInterface customerDao, PasswordEncoder passwordEncoder, AuthenticationFacadeInterface authenticationFacade) {
        super(customerDao, passwordEncoder, authenticationFacade);
    }

    public List<Customer> findAll() {
        return customerDao.findAll();
    }

    public boolean deleteACustomer(int customerId, HttpSession session) {
        Customer loggedCustomer = getCustomer();
        if (loggedCustomer.getId() == customerId) {
            session.invalidate();
            SecurityContextHolder.clearContext();
            customerDao.deleteById(customerId);
            return true;
        } else {
            customerDao.deleteById(customerId);
            return false;
        }
    }

}
