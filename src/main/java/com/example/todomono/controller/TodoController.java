package com.example.todomono.controller;

import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.form.TodoForm;
import com.example.todomono.form.TodoListForm;
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
@RequestMapping("/todo-lists")
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

    @PostMapping("/{todoListNum}/todos")
    public String createATodo(@PathVariable int todoListNum, @Valid TodoForm todoForm, BindingResult result, Model model) {
        try {
            if (!result.hasErrors()) {
                TodoList todoList = todoListService.findOneByCustomerAndNum(homeService.getCustomer(), todoListNum);
                todoService.createOne(todoList, todoForm);
                todoForm.setLabel(INITIAL_LABEL);
            }
        } catch (EntityAlreadyExistException exception) {
            model.addAttribute("errorMessage", exception.getMessage());
        }
        fillUpTheModel(todoListNum, model);
        return "todo/todo-collection";
    }

    @GetMapping("/{todoListNum}/todos")
    public String showAllTodosOfATodoList(@PathVariable int todoListNum, Model model) {
        TodoForm todoForm = new TodoForm(INITIAL_LABEL);
        model.addAttribute("todoForm", todoForm);
        fillUpTheModel(todoListNum, model);
        return "todo/todo-collection";
    }

    @GetMapping("/{todoListNum}/todos/{todoNum}")
    public String showOneTodoOfATodoList(@PathVariable int todoListNum, @PathVariable int todoNum, Model model) {
        TodoList todoList = todoListService.findOneByCustomerAndNum(homeService.getCustomer(), todoListNum);
        TodoForm todoForm = todoService.getOneByTodoListAndNum(todoList, todoNum).convertToDto();
        model.addAttribute("todoListDto", todoList.convertToDto());
        model.addAttribute("todoForm", todoForm);
        model.addAttribute("title", "Todo");
        return "todo/todo";
    }

    @PutMapping("/{todoListNum}/todos/{todoNum}")
    public String updateATodo(@PathVariable int todoListNum, @PathVariable int todoNum, @Valid TodoForm todoForm, BindingResult result, Model model) {
        TodoList todoList = todoListService.findOneByCustomerAndNum(homeService.getCustomer(), todoListNum);
        todoForm.setNum(todoNum);
        if (!result.hasErrors()) {
            try {
                todoService.updateOneForTodoList(todoList, todoForm);
                long nbTodosNotDone = todoService.findAllByTodoList(todoList).stream().filter(todo -> !todo.isDone()).count();
                TodoListForm todoListForm = todoList.convertToDto();
                todoListForm.setFinished(nbTodosNotDone == 0);
                todoListService.updateOneForCustomer(homeService.getCustomer(), todoListForm);
                return "redirect:/todo-lists/{todoListNum}/todos";
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

    @DeleteMapping("/{todoListNum}/todos/{todoNum}")
    public String deleteATodo(@PathVariable int todoListNum, @PathVariable int todoNum) {
        TodoList todoList = todoListService.findOneByCustomerAndNum(homeService.getCustomer(), todoListNum);
        todoService.deleteOneForTodoList(todoList, todoNum);
        return "redirect:/todo-lists/{todoListNum}/todos";
    }

    private void fillUpTheModel(int todoListNum, Model model) {
        TodoList todoList = todoListService.findOneByCustomerAndNum(homeService.getCustomer(), todoListNum);
        List<TodoForm> todoDtoCollection = todoService.findAllByTodoList(todoList).stream().map(Todo::convertToDto).collect(Collectors.toList());
        model.addAttribute("todoListDto", todoList.convertToDto());
        model.addAttribute("todoDtoCollection", todoDtoCollection);
        model.addAttribute("title", "Todos");
    }

}
