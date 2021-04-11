package com.example.todomono.service;

import com.example.todomono.dao.TodoDaoInterface;
import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.TodoAlreadyExistException;
import com.example.todomono.form.TodoForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private final TodoDaoInterface todoDao;

    public TodoService(TodoDaoInterface todoDao) {
        this.todoDao = todoDao;
    }

    public List<Todo> findAllByTodoList(TodoList todoList) {
        return todoDao.findAllByTodoList(todoList);
    }

    public Todo createOne(TodoList todoList, TodoForm todoForm) throws TodoAlreadyExistException {
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
