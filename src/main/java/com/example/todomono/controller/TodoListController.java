package com.example.todomono.controller;

import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.form.TodoListForm;
import com.example.todomono.service.CustomerService;
import com.example.todomono.service.TodoListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TodoListController {

    private static final String INITIAL_TITLE = "New todo-list";

    @Autowired
    private final CustomerService customerService;

    @Autowired
    private final TodoListService todoListService;

    public TodoListController(CustomerService customerService, TodoListService todoListService) {
        this.customerService = customerService;
        this.todoListService = todoListService;
    }

    @PostMapping("/todo-lists")
    public String createATodoList(@Valid TodoListForm todoListForm, BindingResult result, Model model) {
        try {
            if (!result.hasErrors()) {
                todoListService.createOneForCustomer(customerService.getCustomer(), todoListForm);
                todoListForm.setTitle(INITIAL_TITLE);
            }
        } catch (EntityAlreadyExistException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
        }
        fillUpTheModel(model);
        return "todo-list-collection";
    }

    @GetMapping("/todo-lists")
    public String showAllTodoListsOfACustomer(Model model) {
        TodoListForm todoListForm = new TodoListForm(INITIAL_TITLE);
        model.addAttribute("todoListForm", todoListForm);
        fillUpTheModel(model);
        return "todo-list-collection";
    }

    @GetMapping("/todo-lists/{todoListNum}")
    public String showOneTodoListOfACustomer(@PathVariable long todoListNum, Model model) {
        TodoListForm todoListForm = todoListService.getOneByCustomerAndNum(customerService.getCustomer(), todoListNum).convertToDto();
        model.addAttribute("todoListForm", todoListForm);
        model.addAttribute("title", "Todo-List");
        return "todo-list";
    }

    @PutMapping("/todo-lists/{todoListNum}")
    public String updateATodoList(@PathVariable long todoListNum, @Valid TodoListForm todoListForm, BindingResult result, Model model) {
        todoListForm.setNum(todoListNum);
        if (!result.hasErrors()) {
            try {
                todoListService.updateOneForCustomer(customerService.getCustomer(), todoListForm);
                return "redirect:/todo-lists";
            } catch (EntityAlreadyExistException e) {
                model.addAttribute("errorMessage", e.getMessage());
            }
        }
        else {
            model.addAttribute("errorMessage", "ERROR");
        }
        model.addAttribute("title", "Todo-List");
        return "todo-list";
    }

    @DeleteMapping("/todo-lists/{todoListNum}")
    public String deleteATodoList(@PathVariable long todoListNum) {
        todoListService.deleteOneForCustomer(customerService.getCustomer(), todoListNum);
        return "redirect:/todo-lists";
    }

    private void fillUpTheModel(Model model) {
        List<TodoListForm> todoListDtoCollection = todoListService.findAllByCustomer(customerService.getCustomer()).stream().map(TodoList::convertToDto).collect(Collectors.toList());
        model.addAttribute("todoListDtoCollection", todoListDtoCollection);
        model.addAttribute("title", "Todo-Lists");
    }

}
