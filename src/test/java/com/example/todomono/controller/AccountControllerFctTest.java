package com.example.todomono.controller;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.exception.DaoEntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.web.util.UriUtils.encode;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerFctTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerDaoInterface customerDao;

    @Test
    @WithUserDetails(value="greg", userDetailsServiceBeanName="myUserDetailsService")
    void updateName() throws Exception {
        // Given
        String newName = "gre";
        // When
        mockMvc.perform(
                put("/account/change-name")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("name", "UTF-8") + "=" + encode(newName, "UTF-8") + "&" +
                                encode("password", "UTF-8") + "=" + encode("gg", "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/account"));
        // Then
        Customer customer = customerDao.findByName(newName);
        assertNotNull(customer);
        assertThrows(DaoEntityNotFoundException.class, () -> customerDao.findByName("greg"));
    }

}