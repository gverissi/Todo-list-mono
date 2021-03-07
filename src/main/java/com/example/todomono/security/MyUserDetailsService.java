package com.example.todomono.security;

import com.example.todomono.entity.Customer;
import com.example.todomono.repository.CustomerRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private final CustomerRepositoryInterface customerRepository;

    public MyUserDetailsService(CustomerRepositoryInterface customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByName(name);
        if (customer == null) throw new UsernameNotFoundException(name);
        return new MyUserDetails(customer);
    }

}
