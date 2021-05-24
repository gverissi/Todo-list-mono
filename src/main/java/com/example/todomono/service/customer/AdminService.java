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

    /**
     * Find all Customer.
     * @return List of Customers.
     */
    public List<Customer> findAll() {
        return customerDao.findAll();
    }

    /**
     * Find one Customer according to his id.
     * If the Customer doesn't exist throw a DaoEntityNotFoundException and the 404 error page is displayed.
     * @param customerId Customer's id.
     * @return The Customer entity.
     */
    public Customer findOneCustomer(long customerId) {
        return customerDao.findById(customerId);
    }

    /**
     * Delete a Customer account by an Admin. The Admin cant delete his won account. In this case,
     * the session is cleared to be sure the Admin is not considered has logged in anymore.
     * @param customerId Customer's id.
     * @param session Admin's session.
     * @return true if the Admin has deleted his won account. False otherwise.
     */
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

    /**
     * An Admin can update the Roles of a Customer an if he's enabled to log in.
     * @param customerUpdateForm Dto containing the modifications.
     * @param newRoles List of Roles to be associated with the Customer.
     */
    public void updateOneCustomer(CustomerUpdateForm customerUpdateForm, List<Role> newRoles) {
        Customer customer = customerDao.getOne(customerUpdateForm.getId());
        customer.clearRoles();
        newRoles.forEach(customer::addRole);
        customer.setEnabled(customerUpdateForm.isEnabled());
        customerDao.save(customer);
    }

}
