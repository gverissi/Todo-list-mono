package com.example.todomono.controller;

import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.exception.WrongPasswordException;
import com.example.todomono.form.ChangeCustomerNameForm;
import com.example.todomono.service.AccountService;
import com.example.todomono.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AccountController {

    private final HomeService homeService;
    private final AccountService accountService;

    @Autowired
    public AccountController(HomeService homeService, AccountService accountService) {
        this.homeService = homeService;
        this.accountService = accountService;
    }

    @GetMapping("/account")
    public String showAccountPage(Model model) {
        model.addAttribute("title", "Account");
        return "account/account";
    }

    @GetMapping("/change-name")
    public String showChangeNameForm(Model model) {
        model.addAttribute("changeCustomerNameForm", new ChangeCustomerNameForm());
        model.addAttribute("title", "Change Name");
        return "account/change-name";
    }

    @PostMapping("/change-name")
    public String updateName(@Valid ChangeCustomerNameForm changeCustomerNameForm, BindingResult result, Model model) {
        model.addAttribute("title", "Change Name");
        try {
            if (!result.hasErrors()) {
                accountService.updateNameOfACustomer(homeService.getCustomer(), changeCustomerNameForm);
                return "redirect:account";
            } else {
                return "account/change-name";
            }
        } catch (EntityAlreadyExistException | WrongPasswordException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "account/change-name";
        }
    }

}
