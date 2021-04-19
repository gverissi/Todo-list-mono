package com.example.todomono.dao.database;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.repository.CustomerRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDaoDb implements CustomerDaoInterface {

    private final CustomerRepositoryInterface customerRepository;

    @Autowired
    public CustomerDaoDb(CustomerRepositoryInterface customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer findByName(String name) {
        return customerRepository.findByName(name);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public void deleteById(int customerId) {
        customerRepository.deleteById(customerId);
    }

}
