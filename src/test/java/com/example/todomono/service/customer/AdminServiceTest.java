package com.example.todomono.service.customer;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.Role;
import com.example.todomono.form.CustomerUpdateForm;
import com.example.todomono.security.AuthenticationFacadeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    CustomerDaoInterface customerDaoMock = mock(CustomerDaoInterface.class);
    PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
    AuthenticationFacadeInterface authenticationFacadeMock = mock(AuthenticationFacadeInterface.class);

    Authentication authenticationMock = mock(Authentication.class);
    HttpSession httpSessionMock = mock(HttpSession.class);

    String loggedCustomerName = "greg";
    String loggedEncodedPassword = "AbCd";
    Customer loggedCustomer = new Customer(loggedCustomerName, loggedEncodedPassword);

    String customerName = "toto";
    String encodedPassword = "AbCdEf";
    Customer customer = new Customer(customerName, encodedPassword);

    Role role1 = new Role("role 1");
    Role role2 = new Role("role 2");
    Role role3 = new Role("role 3");

    AdminService adminService = new AdminService(customerDaoMock, passwordEncoderMock, authenticationFacadeMock);

    @BeforeEach
    void init() {
        loggedCustomer.setId(1L);
        customer.setId(2L);
        customer.setEnabled(true);
        customer.addRole(role1);
        when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getName()).thenReturn(loggedCustomerName);
        when(customerDaoMock.findByName(loggedCustomerName)).thenReturn(loggedCustomer);
    }

    @Test
    void findAll() {
        // Given a Customer with role ADMIN
        // When
        adminService.findAll();
        // Then
        verify(customerDaoMock).findAll();
    }

    @Test
    void findOneCustomer() {
        // Given a Customer with role ADMIN
        // When
        adminService.findOneCustomer(customer.getId());
        // Then
        verify(customerDaoMock).findById(customer.getId());
    }

    @Test
    void givenAnAdmin_wenDeleteOneCustomer_thenReturnFalse() {
        // Given a Customer with role ADMIN
        // When
        boolean isDeletedCustomerLoggedIn = adminService.deleteOneCustomer(customer.getId(), httpSessionMock);
        // Then
        verify(customerDaoMock).deleteById(customer.getId());
        assertFalse(isDeletedCustomerLoggedIn);
    }

    @Test
    void givenAnAdmin_wenDeleteSelfCustomer_thenReturnTrue() {
        // Given a Customer with role ADMIN
        // When
        boolean isDeletedCustomerLoggedIn = adminService.deleteOneCustomer(loggedCustomer.getId(), httpSessionMock);
        // Then
        verify(customerDaoMock).deleteById(loggedCustomer.getId());
        assertTrue(isDeletedCustomerLoggedIn);
    }

    @Test
    void updateOneCustomer() {
        // Given a Customer with role ADMIN
        CustomerUpdateForm customerUpdateForm = new CustomerUpdateForm();
        customerUpdateForm.setId(customer.getId());
        customerUpdateForm.setEnabled(false);
        when(customerDaoMock.getOne(customerUpdateForm.getId())).thenReturn(customer);
        List<Role> newRoles = new ArrayList<>();
        newRoles.add(role2);
        newRoles.add(role3);
        // When
        adminService.updateOneCustomer(customerUpdateForm, newRoles);
        // Then
        verify(customerDaoMock).getOne(customerUpdateForm.getId());
        verify(customerDaoMock).save(customer);
        assertFalse(customer.isEnabled());
        assertEquals(2, customer.getRoleSet().size());
        assertFalse(customer.getRoleSet().contains(role1));
        assertTrue(customer.getRoleSet().contains(role2));
        assertTrue(customer.getRoleSet().contains(role3));
    }

}