package com.example.todomono.controller;

import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.form.CustomerForm;
import com.example.todomono.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@WebMvcTest(controllers = CustomerController.class, useDefaultFilters = false)
class CustomerControllerUnitTest {

    @MockBean
    private CustomerService customerService;

    @MockBean
    private BindingResult result;

    @MockBean
    private Model model;

    private CustomerController customerController;

    @BeforeEach
    void init() {
        customerController = new CustomerController(customerService);
    }

    @Test
    void showHomePage() {
        // When
        String viewName = customerController.showHomePage(model);
        // Then
        assertEquals("home", viewName);
    }

    @Test
    void showLogInPage() {
        // When
        String viewName = customerController.showLogInPage(model);
        // Then
        assertEquals("log-in", viewName);
    }

    @Test
    void showRegistrationForm() {
        // When
        String viewName = customerController.showRegistrationForm(model);
        // Then
        assertEquals("sign-up", viewName);
    }

    @Test
    void registerNewCustomer() throws EntityAlreadyExistException {
        // Given
        CustomerForm customerForm = new CustomerForm("toto", "1234", "1234");
        // When
        String viewName = customerController.registerNewCustomer(customerForm, result, model);
        // Then
        verify(customerService).createCustomer(customerForm.getName(), customerForm.getPassword());
        assertEquals("redirect:log-in?registered", viewName);
    }

}