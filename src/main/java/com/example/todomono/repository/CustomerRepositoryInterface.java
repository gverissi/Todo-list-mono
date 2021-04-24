package com.example.todomono.repository;

import com.example.todomono.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepositoryInterface extends JpaRepository<Customer, Long> {

    Customer findByName(String name);

}
