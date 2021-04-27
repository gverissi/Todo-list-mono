package com.example.todomono.controller;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.web.util.UriUtils.encode;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerFctTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerDaoInterface customerDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registerNewCustomer() throws Exception {

        String name = "toto";
        String password = "1234";

        mockMvc.perform(
                post("/home/sign-up")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("name", "UTF-8") + "=" + encode(name, "UTF-8")
                                + "&" + encode("password", "UTF-8") + "=" + encode(password, "UTF-8")
                                + "&" + encode("matchingPassword", "UTF-8") + "=" + encode(password, "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/home/log-in?registered"));

        Customer customer = customerDao.findByName(name);
        assertEquals(name, customer.getName());
        assertTrue(passwordEncoder.matches(password, customer.getEncodedPassword()));
        assertTrue(customer.getRoleSet().stream().map(Role::getRoleName).collect(Collectors.toList()).contains("USER"));
        assertFalse(customer.getRoleSet().stream().map(Role::getRoleName).collect(Collectors.toList()).contains("ADMIN"));
    }

}