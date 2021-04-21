package com.example.todomono.controller;

import com.example.todomono.entity.Customer;
import com.example.todomono.entity.Role;
import com.example.todomono.form.CustomerUpdateForm;
import com.example.todomono.form.RoleUpdateForm;
import com.example.todomono.service.RoleService;
import com.example.todomono.service.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CustomerController {

    private final CustomerService customerService;
    private final RoleService roleService;

    @Autowired
    public CustomerController(CustomerService customerService, RoleService roleService) {
        this.customerService = customerService;
        this.roleService = roleService;
    }

    @GetMapping("/customers")
    public String showAllCustomers(Model model) {
        fillUpTheModel(model);
        return "customer/customer-collection";
    }

    @GetMapping("/customers/{customerId}")
    public String showOneCustomer(@PathVariable int customerId, Model model) {
        Customer customer = customerService.getOneById(customerId);
        CustomerUpdateForm customerUpdateForm = new CustomerUpdateForm(customer.getId(), customer.getName(), customer.isEnabled());
        List<Role> roles = roleService.findAll();
        List<String> customerRoleNames = customer.getRoleSet().stream().map(Role::getRoleName).collect(Collectors.toList());
        customerUpdateForm.setRoles(roles.stream().map(Role::convertToRoleUpdateForm).collect(Collectors.toList()));
        customerUpdateForm.getRoles().forEach(roleUpdateForm -> {
            if (customerRoleNames.contains(roleUpdateForm.getRoleName())) {
                roleUpdateForm.setEnabled(true);
            }
        });
        model.addAttribute("customerUpdateForm", customerUpdateForm);
        model.addAttribute("title", "Customer");
        return "customer/customer";
    }

    @PutMapping("/customers/{customerId}")
    public String updateOneCustomer(@PathVariable int customerId, @Valid CustomerUpdateForm customerUpdateForm) {
        customerUpdateForm.setId(customerId);
        List<Role> newRoles = customerUpdateForm.getRoles().stream().filter(RoleUpdateForm::isEnabled).map(roleUpdateForm -> roleService.findById(roleUpdateForm.getId())).collect(Collectors.toList());
        customerService.updateOneCustomer(customerUpdateForm, newRoles);
        return "redirect:/customers";
    }

    @DeleteMapping("/customers/{customerId}")
    public String deleteOneCustomer(@PathVariable int customerId, HttpServletRequest request) {
        boolean isDeletedCustomerLoggedIn = customerService.deleteOneCustomer(customerId, request.getSession());
        if (isDeletedCustomerLoggedIn) return "redirect:/home?delete";
        return "redirect:/customers";
    }

    private void fillUpTheModel(Model model) {
        List<Customer> customerCollection = customerService.findAll();
        model.addAttribute("customerCollection", customerCollection);
        model.addAttribute("title", "Customers");
    }

}
