package com.example.todomono.controller;

import com.example.todomono.form.CustomerForm;
import com.example.todomono.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.verify;

@SpringBootTest
class CustomerControllerUnitTest {

    @MockBean
    private CustomerService customerService;

    @MockBean
    private BindingResult result;

    @MockBean
    private Model model;

    @Test
    void registerNewCustomer() {
        CustomerForm customerForm = new CustomerForm("toto", "1234", "1234");
        CustomerController customerController = new CustomerController(customerService);
        customerController.registerNewCustomer(customerForm, result, model);
        verify(customerService).createCustomer(customerForm.getName(), customerForm.getPassword());
    }

}