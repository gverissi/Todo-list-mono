package com.example.todomono.controller;

import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.exception.WrongPasswordException;
import com.example.todomono.form.CustomerChangeNameForm;
import com.example.todomono.form.CustomerChangePasswordForm;
import com.example.todomono.form.CustomerDeleteAccountForm;
import com.example.todomono.service.TodoListService;
import com.example.todomono.service.TodoService;
import com.example.todomono.service.customer.AccountService;
import com.example.todomono.service.customer.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final HomeService homeService;
    private final AccountService accountService;
    private final TodoListService todoListService;
    private final TodoService todoService;

    @Autowired
    public AccountController(HomeService homeService, AccountService accountService, TodoListService todoListService, TodoService todoService) {
        this.homeService = homeService;
        this.accountService = accountService;
        this.todoListService = todoListService;
        this.todoService = todoService;
    }

    @GetMapping()
    public String showAccountPage(Model model) {
        model.addAttribute("title", "Account");
        List<TodoList> todoLists = todoListService.findAllByCustomer(homeService.getCustomer());
        long totalTodoLists = todoLists.size();
        model.addAttribute("totalTodoLists", totalTodoLists);
        long finishedTodoLists = todoListService.findAllByCustomer(homeService.getCustomer()).stream().filter(TodoList::isFinished).count();
        model.addAttribute("finishedTodoLists", finishedTodoLists);
        long totalTodos = todoLists.stream().mapToLong(todoList -> todoService.findAllByTodoList(todoList).size()).sum();
        model.addAttribute("totalTodos", totalTodos);
        long doneTodos = todoLists.stream().mapToLong(todoList -> todoService.findAllByTodoList(todoList).stream().filter(Todo::isDone).count()).sum();
        model.addAttribute("doneTodos", doneTodos);
        return "account/account";
    }

    @GetMapping("/change-name")
    public String showChangeNameForm(Model model) {
        model.addAttribute("customerChangeNameForm", new CustomerChangeNameForm());
        model.addAttribute("title", "Change Name");
        return "account/change-name";
    }

    @PutMapping("/change-name")
    public String updateName(@Valid CustomerChangeNameForm customerChangeNameForm, BindingResult result, Model model) {
        model.addAttribute("title", "Change Name");
        try {
            if (!result.hasErrors()) {
                accountService.updateNameOfACustomer(homeService.getCustomer(), customerChangeNameForm);
                return "redirect:/account";
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

    @PutMapping("/change-password")
    public String updatePassword(@Valid CustomerChangePasswordForm customerChangePasswordForm, BindingResult result, Model model) {
        model.addAttribute("title", "Change Password");
        try {
            if (!result.hasErrors()) {
                accountService.updatePasswordOfACustomer(homeService.getCustomer(), customerChangePasswordForm);
                return "redirect:/account";
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
                return "redirect:/home?delete";
            } else {
                return "account/delete-account";
            }
        } catch (WrongPasswordException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
            return "account/delete-account";
        }
    }

}
