package com.example.todomono.controller;

import com.example.todomono.entity.Customer;
import com.example.todomono.entity.Role;
import com.example.todomono.form.CustomerUpdateForm;
import com.example.todomono.form.RoleUpdateForm;
import com.example.todomono.service.RoleService;
import com.example.todomono.service.customer.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final RoleService roleService;

    @Autowired
    public AdminController(AdminService adminService, RoleService roleService) {
        this.adminService = adminService;
        this.roleService = roleService;
    }

    @GetMapping("/customers")
    public String showAllCustomers(Model model) {
        fillUpTheModel(model);
        return "admin/customer-collection";
    }

    @GetMapping("/customers/{customerId}")
    public String showOneCustomer(@PathVariable long customerId, Model model) {
        Customer customer = adminService.findOneCustomer(customerId);
        CustomerUpdateForm customerUpdateForm = customer.convertToCustomerUpdateForm();
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
        return "admin/customer";
    }

    @PutMapping("/customers/{customerId}")
    public String updateOneCustomer(@PathVariable long customerId, @Valid CustomerUpdateForm customerUpdateForm) {
        customerUpdateForm.setId(customerId);
        List<Role> newRoles = customerUpdateForm.getRoles().stream().filter(RoleUpdateForm::isEnabled).map(roleUpdateForm -> roleService.getOne(roleUpdateForm.getId())).collect(Collectors.toList());
        adminService.updateOneCustomer(customerUpdateForm, newRoles);
        return "redirect:/admin/customers";
    }

    @DeleteMapping("/customers/{customerId}")
    public String deleteOneCustomer(@PathVariable long customerId, HttpServletRequest request) {
        boolean isDeletedCustomerLoggedIn = adminService.deleteOneCustomer(customerId, request.getSession());
        if (isDeletedCustomerLoggedIn) return "redirect:/home?delete";
        return "redirect:/admin/customers";
    }

    private void fillUpTheModel(Model model) {
        List<Customer> customerCollection = adminService.findAll();
        model.addAttribute("customerCollection", customerCollection);
        model.addAttribute("title", "Customers");
    }

}
