package com.example.todomono.controller;

import com.example.todomono.entity.Role;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.form.CustomerCreateForm;
import com.example.todomono.service.RoleService;
import com.example.todomono.service.customer.HomeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = HomeController.class, useDefaultFilters = false)
class HomeControllerUnitTest {

    @MockBean
    private HomeService homeService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private BindingResult result;

    @MockBean
    private Model model;

    private HomeController homeController;

    @BeforeEach
    void init() {
        homeController = new HomeController(homeService, roleService);
    }

    @Test
    void showHomePage() {
        // When
        String viewName = homeController.showHomePage(model);
        // Then
        assertEquals("home/home", viewName);
    }

    @Test
    void showLogInPage() {
        // When
        String viewName = homeController.showLogInPage(model);
        // Then
        assertEquals("home/log-in", viewName);
    }

    @Test
    void showRegistrationForm() {
        // When
        String viewName = homeController.showRegistrationForm(model);
        // Then
        assertEquals("home/sign-up", viewName);
    }

    @Test
    void registerNewCustomer() throws EntityAlreadyExistException {
        // Given
        CustomerCreateForm customerCreateForm = new CustomerCreateForm("toto", "1234", "1234");
        Role roleMock = mock(Role.class);
        when(roleService.findByRoleName("USER")).thenReturn(roleMock);
        // When
        String viewName = homeController.registerNewCustomer(customerCreateForm, result, model);
        // Then
        verify(homeService).createCustomer(customerCreateForm, roleMock);
        assertEquals("redirect:/home/log-in?registered", viewName);
    }

}