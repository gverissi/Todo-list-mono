package com.example.todomono.security;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final CustomerDaoInterface customerDao;

    @Autowired
    public MyUserDetailsService(CustomerDaoInterface customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Customer customer = customerDao.findByName(name);
        if (customer == null) throw new UsernameNotFoundException(name);
        return new MyUserDetails(customer);
    }

}
