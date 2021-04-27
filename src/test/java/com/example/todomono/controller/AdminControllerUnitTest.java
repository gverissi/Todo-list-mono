package com.example.todomono.controller;

import com.example.todomono.entity.Customer;
import com.example.todomono.form.CustomerUpdateForm;
import com.example.todomono.service.RoleService;
import com.example.todomono.service.customer.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = AdminController.class, useDefaultFilters = false)
class AdminControllerUnitTest {

    @MockBean
    private AdminService adminService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private Model model;

    private AdminController adminController;

    @BeforeEach
    void init() {
        adminController = new AdminController(adminService, roleService);
    }

    @Test
    void showAllCustomers() {
        // When
        String viewName = adminController.showAllCustomers(model);
        // Then
        assertEquals("admin/customer-collection", viewName);
    }

    @Test
    void showOneCustomer() {
        // Given
        Customer customerMock = mock(Customer.class);
        when(adminService.findOneCustomer(1L)).thenReturn(customerMock);
        when(customerMock.convertToCustomerUpdateForm()).thenReturn(mock(CustomerUpdateForm.class));
        // When
        String viewName = adminController.showOneCustomer(1L, model);
        // Then
        assertEquals("admin/customer", viewName);
    }

    @Test
    void updateOneCustomer() {
        // Given
        CustomerUpdateForm customerUpdateForm = new CustomerUpdateForm(1L, "greg", true);
        // When
        String viewName = adminController.updateOneCustomer(1L, customerUpdateForm);
        // Then
        assertEquals("redirect:/admin/customers", viewName);
    }

    @Test
    void deleteOneCustomer() {
        // Given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        // When
        String viewName = adminController.deleteOneCustomer(1L, requestMock);
        // Then
        assertEquals("redirect:/admin/customers", viewName);
    }

}