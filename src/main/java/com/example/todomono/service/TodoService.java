package com.example.todomono.service;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.dao.TodoDaoInterface;
import com.example.todomono.dao.TodoListDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.TodoAlreadyExistException;
import com.example.todomono.exception.TodoListAlreadyExistException;
import com.example.todomono.form.TodoForm;
import com.example.todomono.form.TodoListForm;
import com.example.todomono.security.AuthenticationFacadeInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private final TodoDaoInterface todoDao;

    @Autowired
    private final CustomerDaoInterface customerDao;

    @Autowired
    private final AuthenticationFacadeInterface authenticationFacade;


    public TodoService(TodoDaoInterface todoDao, CustomerDaoInterface customerDao, AuthenticationFacadeInterface authenticationFacade) {
        this.todoDao = todoDao;
        this.customerDao = customerDao;
        this.authenticationFacade = authenticationFacade;
    }

    public List<Todo> findAllByTodoList(TodoList todoList) {
        return todoDao.findAllByTodoList(todoList);
    }

    public Todo createOne(TodoForm todoForm, TodoList todoList) throws TodoAlreadyExistException {
        String label = todoForm.getLabel();
        if (todoExists(todoList, label)) throw new TodoAlreadyExistException("There is already a todo with label: " + label + ".");
        Todo todo = new Todo(label);
        todo.setTodoList(todoList);
        todo.setNum(todoDao.countByTodoList(todoList) + 1);
        return todoDao.save(todo);
    }

    private boolean todoExists(TodoList todoList, String label) {
        return todoDao.findByTodoListAndLabel(todoList, label) != null;
    }

}
