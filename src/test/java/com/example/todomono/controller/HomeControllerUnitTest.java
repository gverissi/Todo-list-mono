package com.example.todomono.controller;

import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.form.CustomerForm;
import com.example.todomono.service.HomeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@WebMvcTest(controllers = HomeController.class, useDefaultFilters = false)
class HomeControllerUnitTest {

    @MockBean
    private HomeService homeService;

    @MockBean
    private BindingResult result;

    @MockBean
    private Model model;

    private HomeController homeController;

    @BeforeEach
    void init() {
        homeController = new HomeController(homeService);
    }

    @Test
    void showHomePage() {
        // When
        String viewName = homeController.showHomePage(model);
        // Then
        assertEquals("home", viewName);
    }

    @Test
    void showLogInPage() {
        // When
        String viewName = homeController.showLogInPage(model);
        // Then
        assertEquals("log-in", viewName);
    }

    @Test
    void showRegistrationForm() {
        // When
        String viewName = homeController.showRegistrationForm(model);
        // Then
        assertEquals("sign-up", viewName);
    }

    @Test
    void registerNewCustomer() throws EntityAlreadyExistException {
        // Given
        CustomerForm customerForm = new CustomerForm("toto", "1234", "1234");
        // When
        String viewName = homeController.registerNewCustomer(customerForm, result, model);
        // Then
        verify(homeService).createCustomer(customerForm.getName(), customerForm.getPassword());
        assertEquals("redirect:log-in?registered", viewName);
    }

}