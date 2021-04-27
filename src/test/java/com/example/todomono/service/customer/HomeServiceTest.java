package com.example.todomono.service.customer;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.Role;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.form.CustomerCreateForm;
import com.example.todomono.security.AuthenticationFacadeInterface;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HomeServiceTest {

    @Test
    void createCustomer() throws EntityAlreadyExistException {
        String name = "toto";
        String password = "1234";
        String encodedPassword = "AbCd";
        CustomerCreateForm customerCreateForm = new CustomerCreateForm(name, password, password);

        Role roleMock = mock(Role.class);
        CustomerDaoInterface customerDaoMock = mock(CustomerDaoInterface.class);
        when(customerDaoMock.findByName(name)).thenReturn(null);
        when(customerDaoMock.save(any(Customer.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
        when(passwordEncoderMock.encode(password)).thenReturn(encodedPassword);

        AuthenticationFacadeInterface authenticationFacadeMock = mock(AuthenticationFacadeInterface.class);

        HomeService homeService = new HomeService(customerDaoMock, passwordEncoderMock, authenticationFacadeMock);
        Customer createdCustomer = homeService.createCustomer(customerCreateForm, roleMock);

        assertEquals(name, createdCustomer.getName());
        assertEquals(encodedPassword, createdCustomer.getEncodedPassword());
        assertEquals(1, createdCustomer.getRoleSet().size());
        assertTrue(createdCustomer.getRoleSet().contains(roleMock));

        verify(customerDaoMock).save(createdCustomer);
        verify(passwordEncoderMock).encode(password);
    }

}