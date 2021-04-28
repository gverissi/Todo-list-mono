package com.example.todomono.controller;

import com.example.todomono.entity.Customer;
import com.example.todomono.form.CustomerChangeNameForm;
import com.example.todomono.form.CustomerChangePasswordForm;
import com.example.todomono.form.CustomerDeleteAccountForm;
import com.example.todomono.service.customer.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriUtils.encode;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    @WithMockUser(roles = "USER", username = "greg")
    void showAccountPage() throws Exception {
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/account"))
                .andExpect(model().attribute("title", "Account"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void showChangeNameForm() throws Exception {
        mockMvc.perform(get("/account/change-name"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/change-name"))
                .andExpect(model().attribute("title", "Change Name"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "greg")
    void updateName() throws Exception {
        doNothing().when(accountService).updateNameOfACustomer(isA(Customer.class), isA(CustomerChangeNameForm.class));
        mockMvc.perform(
                put("/account/change-name")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("name", "UTF-8") + "=" + encode("gre", "UTF-8") + "&" +
                                encode("password", "UTF-8") + "=" + encode("1234", "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/account"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void showChangePasswordForm() throws Exception {
        mockMvc.perform(get("/account/change-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/change-password"))
                .andExpect(model().attribute("title", "Change Password"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "greg")
    void updatePassword() throws Exception {
        doNothing().when(accountService).updatePasswordOfACustomer(isA(Customer.class), isA(CustomerChangePasswordForm.class));
        mockMvc.perform(
                put("/account/change-password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("oldPassword", "UTF-8") + "=" + encode("1234", "UTF-8") + "&" +
                                encode("password", "UTF-8") + "=" + encode("12345", "UTF-8") + "&" +
                                encode("matchingPassword", "UTF-8") + "=" + encode("12345", "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/account"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void showDeleteAccountForm() throws Exception {
        mockMvc.perform(get("/account/delete-account"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/delete-account"))
                .andExpect(model().attribute("title", "Delete Account"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "greg")
    void deleteAccount() throws Exception {
        doNothing().when(accountService).deleteAccount(isA(Customer.class), isA(CustomerDeleteAccountForm.class), isA(HttpSession.class));
        mockMvc.perform(
                delete("/account/delete-account")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("password", "UTF-8") + "=" + encode("12345", "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/home?delete"));
    }

}