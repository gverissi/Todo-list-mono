package com.example.todomono.controller;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.web.util.UriUtils.encode;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerFctTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerDaoInterface customerDao;

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateOneCustomer() throws Exception {
        // Given
        String userName = "greg";
        // When
        mockMvc.perform(
                put("/admin/customers/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("enabled", "UTF-8") + "=" + encode("false", "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/customers"));
        // Then
        Customer customer = customerDao.findByName(userName);
        assertFalse(customer.isEnabled());
    }

}