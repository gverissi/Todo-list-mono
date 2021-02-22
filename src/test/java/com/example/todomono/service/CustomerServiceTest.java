package com.example.todomono.service;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Test
    void createCustomer() {
        CustomerDaoInterface customerDao = mock(CustomerDaoInterface.class);
        when(customerDao.findByName("greg")).thenReturn(mock(Customer.class));
        CustomerService customerService = new CustomerService(customerDao);

        Customer createdCustomer = customerService.createCustomer("greg", "1234");

        assertEquals(new Customer("greg", "1234"), createdCustomer);

        verify(customerDao).save(createdCustomer);
    }

}