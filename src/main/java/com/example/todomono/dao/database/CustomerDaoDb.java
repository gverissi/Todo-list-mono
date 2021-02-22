package com.example.todomono.dao.database;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDaoDb implements CustomerDaoInterface {

    @Override
    public void save(Customer customer) {

    }

    @Override
    public Customer findByName(String name) {
        return null;
    }

}
