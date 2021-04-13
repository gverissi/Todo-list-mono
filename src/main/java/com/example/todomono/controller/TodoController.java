package com.example.todomono.controller;

import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.TodoAlreadyExistException;
import com.example.todomono.exception.TodoListAlreadyExistException;
import com.example.todomono.exception.TodoNotFoundException;
import com.example.todomono.form.TodoForm;
import com.example.todomono.form.TodoListForm;
import com.example.todomono.service.CustomerService;
import com.example.todomono.service.TodoListService;
import com.example.todomono.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TodoController {

    private static final String INITIAL_LABEL = "New todo";

    @Autowired
    private final CustomerService customerService;

    @Autowired
    private final TodoListService todoListService;

    @Autowired
    private final TodoService todoService;

    public TodoController(CustomerService customerService, TodoListService todoListService, TodoService todoService) {
        this.customerService = customerService;
        this.todoListService = todoListService;
        this.todoService = todoService;
    }

    @PostMapping("/todo-lists/{todoListNum}/todos")
    public String createATodo(@PathVariable long todoListNum, @Valid TodoForm todoForm, BindingResult result, Model model) {
        try {
            if (!result.hasErrors()) {
                TodoList todoList = todoListService.getOneByCustomerAndNum(customerService.getCustomer(), todoListNum);
                todoService.createOne(todoList, todoForm);
                todoForm.setLabel(INITIAL_LABEL);
            }
        } catch (TodoAlreadyExistException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
        }
        fillUpTheModel(todoListNum, model);
        return "todo-collection";
    }

    @GetMapping("/todo-lists/{todoListNum}/todos")
    public String showAllTodosOfATodoList(@PathVariable long todoListNum, Model model) {
        TodoForm todoForm = new TodoForm(INITIAL_LABEL);
        model.addAttribute("todoForm", todoForm);
        fillUpTheModel(todoListNum, model);
        return "todo-collection";
    }

    @GetMapping("/todo-lists/{todoListNum}/todos/{todoNum}")
    public String showOneTodoOfATodoList(@PathVariable long todoListNum, @PathVariable long todoNum, Model model) {
        TodoList todoList = todoListService.getOneByCustomerAndNum(customerService.getCustomer(), todoListNum);
        TodoForm todoForm = todoService.getOneByTodoListAndNum(todoList, todoNum).convertToDto();
        model.addAttribute("todoListDto", todoList.convertToDto());
        model.addAttribute("todoForm", todoForm);
        model.addAttribute("title", "Todo");
        return "todo";
    }

    @PutMapping("/todo-lists/{todoListNum}/todos/{todoNum}")
    public String updateATodo(@PathVariable long todoListNum, @PathVariable long todoNum, @Valid TodoForm todoForm, BindingResult result, Model model) {
        TodoList todoList = todoListService.getOneByCustomerAndNum(customerService.getCustomer(), todoListNum);
        todoForm.setNum(todoNum);
        if (!result.hasErrors()) {
            try {
                todoService.updateOneForTodoList(todoList, todoForm);
                return "redirect:/todo-lists/{todoListNum}/todos";
            } catch (TodoAlreadyExistException | TodoNotFoundException e) {
                model.addAttribute("errorMessage", e.getMessage());
            }
        }
        else {
            model.addAttribute("errorMessage", "ERROR");
        }
        model.addAttribute("todoListDto", todoList.convertToDto());
        model.addAttribute("title", "Todo");
        return "todo";
    }

    @DeleteMapping("/todo-lists/{todoListNum}/todos/{todoNum}")
    public String deleteATodoList(@PathVariable long todoListNum, @PathVariable long todoNum, Model model) {
        TodoList todoList = todoListService.getOneByCustomerAndNum(customerService.getCustomer(), todoListNum);
        try {
            todoService.deleteOneForTodoList(todoList, todoNum);
            return "redirect:/todo-lists/{todoListNum}/todos";
        } catch (TodoNotFoundException e) {
            model.addAttribute("errorMessage", "ERROR");
            model.addAttribute("todoListDto", todoList.convertToDto());
            model.addAttribute("title", "Todo");
            return "todo";
        }
    }

    private void fillUpTheModel(long todoListNum, Model model) {
        TodoList todoList = todoListService.getOneByCustomerAndNum(customerService.getCustomer(), todoListNum);
        List<TodoForm> todoFormCollection = todoService.findAllByTodoList(todoList).stream().map(Todo::convertToDto).collect(Collectors.toList());
        TodoListForm todoListForm = todoList.convertToDto();
        model.addAttribute("todoListForm", todoListForm);
        model.addAttribute("todoFormCollection", todoFormCollection);
        model.addAttribute("title", "Todos");
    }

}
