package com.example.todomono.service.customer;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.exception.WrongPasswordException;
import com.example.todomono.form.CustomerChangeNameForm;
import com.example.todomono.form.CustomerChangePasswordForm;
import com.example.todomono.form.CustomerDeleteAccountForm;
import com.example.todomono.security.AuthenticationFacadeInterface;
import com.example.todomono.security.MyUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

class AccountServiceTest {

    CustomerDaoInterface customerDaoMock = mock(CustomerDaoInterface.class);
    PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
    AuthenticationFacadeInterface authenticationFacadeMock = mock(AuthenticationFacadeInterface.class);

    Authentication authenticationMock = mock(Authentication.class);
    HttpSession httpSessionMock = mock(HttpSession.class);

    String customerName = "toto";
    String encodedPassword = "AbCdEf";
    Customer customer = new Customer(customerName, encodedPassword);

    AccountService accountService = new AccountService(customerDaoMock, passwordEncoderMock, authenticationFacadeMock);

    @BeforeEach
    void init() {
        customer.setId(1L);
        customer.setEnabled(true);
        when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getPrincipal()).thenReturn(mock(MyUserDetails.class));
        when(customerDaoMock.findByName(customerName)).thenReturn(customer);
    }

    @Test
    void updateNameOfACustomer() throws EntityAlreadyExistException, WrongPasswordException {
        // Given
        CustomerChangeNameForm customerChangeNameForm = new CustomerChangeNameForm();
        customerChangeNameForm.setName("gre");
        customerChangeNameForm.setPassword("gg");
        when(passwordEncoderMock.matches(customerChangeNameForm.getPassword(), encodedPassword)).thenReturn(true);
        // When
        accountService.updateNameOfACustomer(customer, customerChangeNameForm);
        // Then
        verify(authenticationFacadeMock).getAuthentication();
        verify(customerDaoMock).save(customer);
    }

    @Test
    void updatePasswordOfACustomer() throws WrongPasswordException {
        // Given
        CustomerChangePasswordForm customerChangePasswordForm = new CustomerChangePasswordForm();
        customerChangePasswordForm.setOldPassword("gg");
        customerChangePasswordForm.setPassword("ggg");
        customerChangePasswordForm.setMatchingPassword("ggg");
        when(passwordEncoderMock.matches(customerChangePasswordForm.getOldPassword(), encodedPassword)).thenReturn(true);
        // When
        accountService.updatePasswordOfACustomer(customer, customerChangePasswordForm);
        // Then
        verify(passwordEncoderMock).encode(customerChangePasswordForm.getPassword());
        verify(authenticationFacadeMock).getAuthentication();
        verify(customerDaoMock).save(customer);
    }

    @Test
    void deleteAccount() throws WrongPasswordException {
        // Given
        CustomerDeleteAccountForm customerDeleteAccountForm = new CustomerDeleteAccountForm();
        customerDeleteAccountForm.setPassword("ggg");
        when(passwordEncoderMock.matches(customerDeleteAccountForm.getPassword(), encodedPassword)).thenReturn(true);
        // When
        accountService.deleteAccount(customer, customerDeleteAccountForm, httpSessionMock);
        // Then
        verify(httpSessionMock).invalidate();
        verify(customerDaoMock).deleteById(customer.getId());
    }

}