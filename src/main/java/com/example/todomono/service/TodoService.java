package com.example.todomono.service;

import com.example.todomono.dao.TodoDaoInterface;
import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.exception.EntityNotFoundException;
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

    public Todo createOne(TodoList todoList, TodoForm todoForm) throws EntityAlreadyExistException {
        String label = todoForm.getLabel();
        if (todoExists(todoList, label)) throw new EntityAlreadyExistException("There is already a todo with label: " + label + ".");
        Todo todo = new Todo(label);
        todo.setTodoList(todoList);
        todo.setNum(todoDao.countByTodoList(todoList) + 1);
        return todoDao.save(todo);
    }

    public List<Todo> findAllByTodoList(TodoList todoList) {
        return todoDao.findAllByTodoList(todoList);
    }

    public Todo getOneByTodoListAndNum(TodoList todoList, long todoNum) {
        Todo todo = todoDao.findByTodoListAndNum(todoList, todoNum);
        if (todo == null) throw new EntityNotFoundException("There is no todo with num = " + todoNum);
        return todo;
    }

    public Todo updateOneForTodoList(TodoList todoList, TodoForm todoForm) throws EntityAlreadyExistException {
        String label = todoForm.getLabel();
        if (todoExists(todoList, label)) throw new EntityAlreadyExistException("There is already a todo with label: " + label + ".");
        Todo todo = todoDao.findByTodoListAndNum(todoList, todoForm.getNum());
        if (todo == null) throw new EntityNotFoundException("There is no todo with num = " + todoForm.getNum());
        todo.setLabel(label);
        return todoDao.save(todo);
    }

    public void deleteOneForTodoList(TodoList todoList, long todoNum) {
        Todo todo = todoDao.findByTodoListAndNum(todoList, todoNum);
        if (todo == null) throw new EntityNotFoundException("There is no todo with num = " + todoNum);
        todoDao.delete(todo);
        computeTodoNum(todoList);
    }

    private boolean todoExists(TodoList todoList, String label) {
        return todoDao.findByTodoListAndLabel(todoList, label) != null;
    }

    private void computeTodoNum(TodoList todoList) {
        long num = 0L;
        List<Todo> todos = todoDao.findAllByTodoList(todoList);
        for (Todo todo : todos) {
            todo.setNum(++num);
            todoDao.save(todo);
        }
    }

}
