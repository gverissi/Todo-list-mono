package com.example.todomono.controller;

import com.example.todomono.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/account")
    public String showHomePage(Model model) {
        model.addAttribute("title", "Account");
        return "account";
    }

    @GetMapping("/update-name")
    public String updateName(Model model) {
        model.addAttribute("errorMessage", "ERROR");
        model.addAttribute("title", "Change Name");
        return "account";
    }

    @GetMapping("/update-password")
    public String updatePassword(Model model) {
        model.addAttribute("errorMessage", "ERROR");
        model.addAttribute("title", "Change Password");
        return "account";
    }

}
