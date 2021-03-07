package com.example.todomono.controller;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.security.MyUserDetailsService;
import com.example.todomono.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriUtils.encode;

@WebMvcTest(CustomerController.class)
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private MyUserDetailsService myUserDetailsService;

    @MockBean
    private CustomerDaoInterface customerDao;

    @MockBean
    private RoleDaoInterface roleDao;

    @Test
    void showHomePage() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("title", "Home"));
    }

    @Test
    void log_in() throws Exception {
        mockMvc.perform(get("/log-in"))
                .andExpect(status().isOk())
                .andExpect(view().name("log-in"))
                .andExpect(model().attribute("title", "Log-In"))
                .andDo(print());
    }

    @Test
    void showRegistrationForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(model().attribute("title", "Sign-Up"))
                .andExpect(model().attributeExists("customerForm"))
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

        mockMvc.perform(
                post("/sign-up")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("name", "UTF-8") + "=" + encode("toto", "UTF-8")
                                + "&" + encode("password", "UTF-8") + "=" + encode("123", "UTF-8")
                                + "&" + encode("matchingPassword", "UTF-8") + "=" + encode("123", "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:log-in?registered"));
    }

}