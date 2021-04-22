package com.example.todomono.controller;

import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.form.TodoForm;
import com.example.todomono.service.customer.HomeService;
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

    private final HomeService homeService;
    private final TodoListService todoListService;
    private final TodoService todoService;

    @Autowired
    public TodoController(HomeService homeService, TodoListService todoListService, TodoService todoService) {
        this.homeService = homeService;
        this.todoListService = todoListService;
        this.todoService = todoService;
    }

    @PostMapping("/todo-lists/{todoListNum}/todos")
    public String createATodo(@PathVariable long todoListNum, @Valid TodoForm todoForm, BindingResult result, Model model) {
        try {
            if (!result.hasErrors()) {
                TodoList todoList = todoListService.getOneByCustomerAndNum(homeService.getCustomer(), todoListNum);
                todoService.createOne(todoList, todoForm);
                todoForm.setLabel(INITIAL_LABEL);
            }
        } catch (EntityAlreadyExistException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
        }
        fillUpTheModel(todoListNum, model);
        return "todo/todo-collection";
    }

    @GetMapping("/todo-lists/{todoListNum}/todos")
    public String showAllTodosOfATodoList(@PathVariable long todoListNum, Model model) {
        TodoForm todoForm = new TodoForm(INITIAL_LABEL);
        model.addAttribute("todoForm", todoForm);
        fillUpTheModel(todoListNum, model);
        return "todo/todo-collection";
    }

    @GetMapping("/todo-lists/{todoListNum}/todos/{todoNum}")
    public String showOneTodoOfATodoList(@PathVariable long todoListNum, @PathVariable long todoNum, Model model) {
        TodoList todoList = todoListService.getOneByCustomerAndNum(homeService.getCustomer(), todoListNum);
        TodoForm todoForm = todoService.getOneByTodoListAndNum(todoList, todoNum).convertToDto();
        model.addAttribute("todoListDto", todoList.convertToDto());
        model.addAttribute("todoForm", todoForm);
        model.addAttribute("title", "Todo");
        return "todo/todo";
    }

    @PutMapping("/todo-lists/{todoListNum}/todos/{todoNum}")
    public String updateATodo(@PathVariable long todoListNum, @PathVariable long todoNum, @Valid TodoForm todoForm, BindingResult result, Model model) {
        TodoList todoList = todoListService.getOneByCustomerAndNum(homeService.getCustomer(), todoListNum);
        todoForm.setNum(todoNum);
        if (!result.hasErrors()) {
            try {
                todoService.updateOneForTodoList(todoList, todoForm);
                return showAllTodosOfATodoList(todoListNum, model);
            } catch (EntityAlreadyExistException e) {
                model.addAttribute("errorMessage", e.getMessage());
            }
        }
        else {
            model.addAttribute("errorMessage", "ERROR");
        }
        model.addAttribute("todoListDto", todoList.convertToDto());
        model.addAttribute("title", "Todo");
        return "todo/todo";
    }

    @DeleteMapping("/todo-lists/{todoListNum}/todos/{todoNum}")
    public String deleteATodo(@PathVariable long todoListNum, @PathVariable long todoNum, Model model) {
        TodoList todoList = todoListService.getOneByCustomerAndNum(homeService.getCustomer(), todoListNum);
        todoService.deleteOneForTodoList(todoList, todoNum);
        return showAllTodosOfATodoList(todoListNum, model);
    }

    private void fillUpTheModel(long todoListNum, Model model) {
        TodoList todoList = todoListService.getOneByCustomerAndNum(homeService.getCustomer(), todoListNum);
        List<TodoForm> todoDtoCollection = todoService.findAllByTodoList(todoList).stream().map(Todo::convertToDto).collect(Collectors.toList());
        model.addAttribute("todoListDto", todoList.convertToDto());
        model.addAttribute("todoDtoCollection", todoDtoCollection);
        model.addAttribute("title", "Todos");
    }

}
