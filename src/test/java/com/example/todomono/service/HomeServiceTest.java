package com.example.todomono.service;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.exception.EntityAlreadyExistException;
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

        CustomerDaoInterface customerDaoMock = mock(CustomerDaoInterface.class);
        when(customerDaoMock.findByName(name)).thenReturn(null);
        when(customerDaoMock.save(any(Customer.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
        when(passwordEncoderMock.encode(password)).thenReturn(encodedPassword);

        AuthenticationFacadeInterface authenticationFacadeMock = mock(AuthenticationFacadeInterface.class);

        HomeService homeService = new HomeService(customerDaoMock, passwordEncoderMock, authenticationFacadeMock);
        Customer createdCustomer = homeService.createCustomer(name, password);

        assertEquals(name, createdCustomer.getName());
        assertEquals(encodedPassword, createdCustomer.getEncodedPassword());

        verify(customerDaoMock).save(createdCustomer);
        verify(passwordEncoderMock).encode(password);
    }

}