package com.example.todomono.controller;

import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.TodoListAlreadyExistException;
import com.example.todomono.form.TodoListForm;
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
    private final TodoListService todoListService;

    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @GetMapping("/todo-lists")
    public String showAllTodoListOfACustomer(Model model) {
        TodoListForm todoListForm = new TodoListForm(INITIAL_TITLE);
        model.addAttribute("todoListForm", todoListForm);
        fillUpTheModel(model);
        return "todo-list-collection";
    }

    @PostMapping("/todo-lists")
    public String createATodoList(@Valid TodoListForm todoListForm, BindingResult result, Model model) {
        try {
            if (!result.hasErrors()) {
                todoListService.createOne(todoListForm);
                todoListForm.setTitle(INITIAL_TITLE);
            }
        } catch (TodoListAlreadyExistException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
        }
        fillUpTheModel(model);
        return "todo-list-collection";
    }

    @GetMapping("/todo-lists/{todoListNum}")
    public String showOneTodoListOfACustomer(@PathVariable long todoListNum, Model model) {
        TodoListForm todoListForm = todoListService.getOneByNum(todoListNum).convertToDto();
        model.addAttribute("todoListForm", todoListForm);
        model.addAttribute("todoListNum", todoListNum);
        model.addAttribute("title", "Todo-List");
        return "todo-list";
    }

    @PutMapping("/todo-lists/{todoListNum}")
    public String updateATodoList(@PathVariable long todoListNum, @Valid TodoListForm todoListForm, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            try {
                todoListService.updateOne(todoListForm);
                return "redirect:/todo-lists";
            } catch (TodoListAlreadyExistException e) {
                model.addAttribute("errorMessage", e.getMessage());
            }
        }
        else {
            model.addAttribute("errorMessage", "ERROR");
        }
        model.addAttribute("todoListNum", todoListNum);
        model.addAttribute("title", "Todo-List");
        return "todo-list";
    }

    @DeleteMapping("/todo-lists/{todoListNum}")
    public String deleteATodoList(@PathVariable long todoListNum) {
        todoListService.deleteOne(todoListNum);
        return "redirect:/todo-lists";
    }

    private void fillUpTheModel(Model model) {
        List<TodoListForm> todoListFormCollection = todoListService.findAllByCustomer().stream().map(TodoList::convertToDto).collect(Collectors.toList());
        model.addAttribute("todoListFormCollection", todoListFormCollection);
        model.addAttribute("title", "Todo-Lists");
    }

}
