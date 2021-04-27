package com.example.todomono.dao.memory;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class CustomerDaoMemoryTest {

    @Test
    void findByName() {
        CustomerDaoInterface customerDao = new CustomerDaoMemory();
        Customer customer = new Customer("greg", "1234");
        customerDao.save(customer);
        assertThat(customerDao.findByName("greg"), equalTo(customer));
    }

}