package com.example.todomono.controller;

import com.example.todomono.entity.Customer;
import com.example.todomono.service.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public String showAllCustomers(Model model) {
        fillUpTheModel(model);
        return "customer/customer-collection";
    }

    @DeleteMapping("/customers/{customerId}")
    public String deleteACustomer(@PathVariable int customerId, HttpServletRequest request) {
        boolean isDeletedCustomerLoggedIn = customerService.deleteACustomer(customerId, request.getSession());
        if (isDeletedCustomerLoggedIn) return "redirect:/home?delete";
        return "redirect:/customers";
    }

    private void fillUpTheModel(Model model) {
        List<Customer> customerCollection = customerService.findAll();
        model.addAttribute("customerCollection", customerCollection);
        model.addAttribute("title", "Customers");
    }

}
