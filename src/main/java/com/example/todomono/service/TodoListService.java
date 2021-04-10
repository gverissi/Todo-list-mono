package com.example.todomono.service;

import com.example.todomono.dao.CustomerDaoInterface;
import com.example.todomono.dao.TodoListDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.EntityNotFoundException;
import com.example.todomono.exception.TodoListAlreadyExistException;
import com.example.todomono.form.TodoListForm;
import com.example.todomono.security.AuthenticationFacadeInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoListService {

    @Autowired
    private final TodoListDaoInterface todoListDao;


    public TodoListService(TodoListDaoInterface todoListDao) {
        this.todoListDao = todoListDao;
    }

    public List<TodoList> findAllByCustomer(Customer customer) {
        return todoListDao.findAllByCustomer(customer);
    }

    public TodoList createOneForCustomer(Customer customer, TodoListForm todoListForm) throws TodoListAlreadyExistException {
        String title = todoListForm.getTitle();
        if (todoListExists(customer, title)) throw new TodoListAlreadyExistException("There is already a todo-list with name: " + title + ".");
        TodoList todoList = new TodoList(title);
        todoList.setCustomer(customer);
        todoList.setNum(todoListDao.countByCustomer(customer) + 1);
        return todoListDao.save(todoList);
    }

    public TodoList getOneByCustomerAndNum(Customer customer, long todoListNum) {
        TodoList todoList = todoListDao.findByCustomerAndNum(customer, todoListNum);
        if (todoList == null) throw new EntityNotFoundException("There is no todo-list with num = " + todoListNum);
        return todoList;
    }

    public TodoList updateOneForCustomer(Customer customer, TodoListForm todoListForm) throws TodoListAlreadyExistException {
        String title = todoListForm.getTitle();
        long todoListNum = todoListForm.getNum();
        if (todoListExists(customer, title)) throw new TodoListAlreadyExistException("There is already a todo-list with name: " + title + ".");
        TodoList todoList = todoListDao.findByCustomerAndNum(customer, todoListNum);
        if (todoList == null) throw new EntityNotFoundException("There is no todo-list with num = " + todoListNum);
        todoList.setTitle(title);
        return todoListDao.save(todoList);
    }

    public void deleteOneForCustomer(Customer customer, long todoListNum) {
        TodoList todoList = todoListDao.findByCustomerAndNum(customer, todoListNum);
        if (todoList == null) throw new EntityNotFoundException("There is no todo-list with num = " + todoListNum);
        todoListDao.delete(todoList);
        computeTodoListNum(customer);
    }

    private boolean todoListExists(Customer customer, String title) {
        return todoListDao.findByCustomerAndTitle(customer, title) != null;
    }

    private void computeTodoListNum(Customer customer) {
        long num = 0L;
        List<TodoList> todoLists = todoListDao.findAllByCustomer(customer);
        for (TodoList todoList : todoLists) {
            todoList.setNum(++num);
            todoListDao.save(todoList);
        }
    }

}
