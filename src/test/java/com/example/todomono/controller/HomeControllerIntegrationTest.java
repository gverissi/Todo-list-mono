package com.example.todomono.controller;

import com.example.todomono.entity.Customer;
import com.example.todomono.entity.Role;
import com.example.todomono.form.CustomerCreateForm;
import com.example.todomono.service.customer.HomeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriUtils.encode;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HomeService homeService;

    @Test
    void showHomePage() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("home/home"))
                .andExpect(model().attribute("title", "Home"));
    }

    @Test
    void showLogInPage() throws Exception {
        mockMvc.perform(get("/home/log-in"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/log-in"))
                .andExpect(model().attribute("title", "Log-In"))
                .andDo(print());
    }

    @Test
    void showRegistrationForm() throws Exception {
        mockMvc.perform(get("/home/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/sign-up"))
                .andExpect(model().attribute("title", "Sign-Up"))
                .andExpect(model().attributeExists("customerCreateForm"))
                .andDo(print());
    }

    @Test
    void registerNewCustomer() throws Exception {

//        mockMvc.perform(
//                post("/sign-up")
//                        .param("name", "someValue")
//                        .param("password", "someValue")
//                        .param("matchingPassword", "someValue")
//                        .with(csrf()))
//                .andExpect(status().isFound());

        Role roleMock = mock(Role.class);

        String customerName = "toto";
        String customerPassword = "123";
        CustomerCreateForm customerCreateForm = new CustomerCreateForm(customerName, customerPassword, customerPassword);
        when(homeService.createCustomer(customerCreateForm, roleMock)).thenReturn(mock(Customer.class));

        mockMvc.perform(
                post("/home/sign-up")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("name", "UTF-8") + "=" + encode(customerName, "UTF-8")
                                + "&" + encode("password", "UTF-8") + "=" + encode(customerPassword, "UTF-8")
                                + "&" + encode("matchingPassword", "UTF-8") + "=" + encode(customerPassword, "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/home/log-in?registered"));
    }

}