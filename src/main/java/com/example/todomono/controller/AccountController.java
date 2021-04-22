package com.example.todomono.controller;

import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.exception.WrongPasswordException;
import com.example.todomono.form.CustomerChangeNameForm;
import com.example.todomono.form.CustomerChangePasswordForm;
import com.example.todomono.form.CustomerDeleteAccountForm;
import com.example.todomono.service.customer.AccountService;
import com.example.todomono.service.customer.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
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
        model.addAttribute("customerChangeNameForm", new CustomerChangeNameForm());
        model.addAttribute("title", "Change Name");
        return "account/change-name";
    }

    @PostMapping("/change-name")
    public String updateName(@Valid CustomerChangeNameForm customerChangeNameForm, BindingResult result, Model model) {
        model.addAttribute("title", "Change Name");
        try {
            if (!result.hasErrors()) {
                accountService.updateNameOfACustomer(homeService.getCustomer(), customerChangeNameForm);
                return "redirect:account";
            } else {
                return "account/change-name";
            }
        } catch (EntityAlreadyExistException | WrongPasswordException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "account/change-name";
        }
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("customerChangePasswordForm", new CustomerChangePasswordForm());
        model.addAttribute("title", "Change Password");
        return "account/change-password";
    }

    @PostMapping("/change-password")
    public String updatePassword(@Valid CustomerChangePasswordForm customerChangePasswordForm, BindingResult result, Model model) {
        model.addAttribute("title", "Change Password");
        try {
            if (!result.hasErrors()) {
                accountService.updatePasswordOfACustomer(homeService.getCustomer(), customerChangePasswordForm);
                return "redirect:account";
            } else {
                return "account/change-password";
            }
        } catch (WrongPasswordException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "account/change-password";
        }
    }

    @GetMapping("/delete-account")
    public String showDeleteAccountForm(Model model) {
        model.addAttribute("customerDeleteAccountForm", new CustomerDeleteAccountForm());
        model.addAttribute("title", "Delete Account");
        return "account/delete-account";
    }

    @DeleteMapping("/delete-account")
    public String deleteAccount(HttpServletRequest request, @Valid CustomerDeleteAccountForm customerDeleteAccountForm, BindingResult result, Model model) {
        model.addAttribute("title", "Delete Account");
        try {
            if (!result.hasErrors()) {
                accountService.deleteAccount(homeService.getCustomer(), customerDeleteAccountForm, request.getSession());
                return "redirect:home?delete";
            } else {
                return "account/delete-account";
            }
        } catch (WrongPasswordException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "account/delete-account";
        }
    }

}
