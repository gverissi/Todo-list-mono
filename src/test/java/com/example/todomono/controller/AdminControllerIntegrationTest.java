package com.example.todomono.controller;

import com.example.todomono.entity.Customer;
import com.example.todomono.form.CustomerUpdateForm;
import com.example.todomono.service.customer.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriUtils.encode;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private HttpServletRequest request;

    @Test
    @WithMockUser(roles = "ADMIN")
    void showAllCustomers() throws Exception {
        mockMvc.perform(get("/admin/customers"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer-collection"))
                .andExpect(model().attribute("title", "Customers"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void showOneCustomer() throws Exception {
        Customer customerMock = mock(Customer.class);
        when(adminService.findOneCustomer(1L)).thenReturn(customerMock);
        when(customerMock.convertToCustomerUpdateForm()).thenReturn(mock(CustomerUpdateForm.class));
        mockMvc.perform(get("/admin/customers/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer"))
                .andExpect(model().attribute("title", "Customer"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateOneCustomer() throws Exception {
        mockMvc.perform(
                put("/admin/customers/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(encode("enabled", "UTF-8") + "=" + encode("false", "UTF-8"))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/customers"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteOneCustomer() throws Exception {
        when(adminService.deleteOneCustomer(1L, request.getSession())).thenReturn(false);
        mockMvc.perform(
                delete("/admin/customers/1").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/customers"));
    }

}