package com.example.todomono.controller;

import com.example.todomono.exception.CustomerAlreadyExistException;
import com.example.todomono.form.CustomerForm;
import com.example.todomono.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class CustomerController {

    @Autowired
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/home")
    public String showHomePage(Model model) {
        model.addAttribute("title", "Home");
        return "home";
    }

    @RequestMapping("/log-in")
    public String log_in(Model model) {
        model.addAttribute("title", "Log-In");
        return "log-in";
    }

    @GetMapping("/sign-up")
    public String showRegistrationForm(@ModelAttribute CustomerForm customerForm, Model model) {
        model.addAttribute("title", "Sign-Up");
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String registerNewCustomer(@Valid CustomerForm customerForm, BindingResult result, Model model) {
        model.addAttribute("title", "Sign-Up");
        try {
            if (!result.hasErrors()) {
                customerService.createCustomer(customerForm.getName(), customerForm.getPassword());
                return "redirect:log-in?registered";
            } else {
                return "sign-up";
            }
        } catch (CustomerAlreadyExistException exception) {
            model.addAttribute("errorMessage", "There is already an account with name: " + customerForm.getName());
            return "sign-up";
        }
    }

}
