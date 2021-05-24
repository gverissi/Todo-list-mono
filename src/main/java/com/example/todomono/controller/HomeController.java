package com.example.todomono.controller;

import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.form.CustomerCreateForm;
import com.example.todomono.service.RoleService;
import com.example.todomono.service.customer.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping({"/home", "/"})
public class HomeController {

    private final HomeService homeService;
    private final RoleService roleService;

    @Autowired
    public HomeController(HomeService homeService, RoleService roleService) {
        this.homeService = homeService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String showHomePage(Model model) {
        model.addAttribute("title", "Home");
        return "home/home";
    }

    @RequestMapping("/log-in")
    public String showLogInPage(Model model) {
        model.addAttribute("title", "Log-In");
        return "home/log-in";
    }

    @GetMapping("/sign-up")
    public String showRegistrationForm(Model model) {
        model.addAttribute("customerCreateForm", new CustomerCreateForm());
        model.addAttribute("title", "Sign-Up");
        return "home/sign-up";
    }

    @PostMapping("/sign-up")
    public String registerNewCustomer(@Valid CustomerCreateForm customerCreateForm, BindingResult result, Model model) {
        model.addAttribute("title", "Sign-Up");
        try {
            if (!result.hasErrors()) {
                homeService.createCustomer(customerCreateForm, roleService.findByRoleName("USER"));
                return "redirect:/home/log-in?registered";
            } else {
                return "home/sign-up";
            }
        } catch (EntityAlreadyExistException exception) {
            model.addAttribute("errorMessage", "There is already an account with name: " + customerCreateForm.getName());
            return "home/sign-up";
        }
    }

}
