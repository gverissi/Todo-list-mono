package com.example.todomono.service.customer;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.Role;
import com.example.todomono.form.CustomerUpdateForm;
import com.example.todomono.security.AuthenticationFacadeInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class AdminService extends AbstractCustomerService {

    @Autowired
    public AdminService(CustomerDaoInterface customerDao, PasswordEncoder passwordEncoder, AuthenticationFacadeInterface authenticationFacade) {
        super(customerDao, passwordEncoder, authenticationFacade);
    }

    public List<Customer> findAll() {
        return customerDao.findAll();
    }

    public Customer findOneCustomer(long customerId) {
        return customerDao.findById(customerId);
    }

    public boolean deleteOneCustomer(long customerId, HttpSession session) {
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

    public void updateOneCustomer(CustomerUpdateForm customerUpdateForm, List<Role> newRoles) {
        Customer customer = customerDao.getOne(customerUpdateForm.getId());
        customer.clearRoles();
        newRoles.forEach(customer::addRole);
        customer.setEnabled(customerUpdateForm.isEnabled());
        customerDao.save(customer);
    }

}
