package com.example.todomono.dao.database;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.exception.DaoEntityNotFoundException;
import com.example.todomono.repository.CustomerRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"prod", "dev"})
public class CustomerDaoDb extends EntityDaoDb<Customer, CustomerRepositoryInterface> implements CustomerDaoInterface {

    private final CustomerRepositoryInterface customerRepository;

    @Autowired
    public CustomerDaoDb(CustomerRepositoryInterface customerRepository) {
        super(customerRepository);
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findByName(String name) throws DaoEntityNotFoundException {
        Customer customer = customerRepository.findByName(name);
        if (customer == null) throw new DaoEntityNotFoundException();
        return customer;
    }

}

