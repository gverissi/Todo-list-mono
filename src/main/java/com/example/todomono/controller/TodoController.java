package com.example.todomono.controller;

import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.TodoAlreadyExistException;
import com.example.todomono.form.TodoForm;
import com.example.todomono.form.TodoListForm;
import com.example.todomono.service.CustomerService;
import com.example.todomono.service.TodoListService;
import com.example.todomono.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/todo-lists/{todoListNum}/todos")
    public String showAllTodoOfATodoList(@PathVariable long todoListNum, Model model) {
        TodoForm todoForm = new TodoForm(INITIAL_LABEL);
        model.addAttribute("todoForm", todoForm);
        fillUpTheModel(todoListNum, model);
        return "todo-collection";
    }

    @PostMapping("/todo-lists/{todoListNum}/todos")
    public String createATodo(@PathVariable long todoListNum, @Valid TodoForm todoForm, BindingResult result, Model model) {
        try {
            if (!result.hasErrors()) {
                TodoList todoList = todoListService.getOneByCustomerAndNum(customerService.getCustomer(), todoListNum);
                todoService.createOne(todoForm, todoList);
                todoForm.setLabel(INITIAL_LABEL);
            }
        } catch (TodoAlreadyExistException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
        }
        fillUpTheModel(todoListNum, model);
        return "todo-collection";
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
