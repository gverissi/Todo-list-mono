package com.example.todomono.service;

import com.example.todomono.dao.TodoDaoInterface;
import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.DaoConstraintViolationException;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.form.TodoForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoDaoInterface todoDao;

    @Autowired
    public TodoService(TodoDaoInterface todoDao) {
        this.todoDao = todoDao;
    }

    public Todo createOne(TodoList todoList, TodoForm todoForm) throws EntityAlreadyExistException {
        Todo todo = new Todo(todoForm.getLabel());
        todo.setTodoList(todoList);
        todo.setNum(todoDao.countByTodoList(todoList) + 1);
        try {
            return todoDao.save(todo);
        } catch (DaoConstraintViolationException e) {
            throw new EntityAlreadyExistException("There is already a todo with label: " + todoForm.getLabel());
        }
    }

    public List<Todo> findAllByTodoList(TodoList todoList) {
        return todoDao.findAllByTodoList(todoList);
    }

    public Todo getOneByTodoListAndNum(TodoList todoList, int todoNum) {
        return todoDao.findByTodoListAndNum(todoList, todoNum);
    }

    public Todo updateOneForTodoList(TodoList todoList, TodoForm todoForm) throws EntityAlreadyExistException {
        String label = todoForm.getLabel();
        Todo todo = todoDao.findByTodoListAndNum(todoList, todoForm.getNum());
        todo.setLabel(label);
        todo.setDone(todoForm.isDone());
        try {
            return todoDao.save(todo);
        } catch (DaoConstraintViolationException e) {
            throw new EntityAlreadyExistException("There is already a todo with label: " + label);
        }
    }

    public void deleteOneForTodoList(TodoList todoList, int todoNum) {
        Todo todo = todoDao.findByTodoListAndNum(todoList, todoNum);
        todoDao.deleteById(todo.getId());
        computeTodoNum(todoList);
    }

    private void computeTodoNum(TodoList todoList) {
        int num = 0;
        List<Todo> todos = todoDao.findAllByTodoList(todoList);
        for (Todo todo : todos) {
            todo.setNum(++num);
            todoDao.save(todo);
        }
    }

}
