package com.example.todomono.service;

import com.example.todomono.dao.TodoListDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.EntityAlreadyExistException;
import com.example.todomono.exception.EntityNotFoundException;
import com.example.todomono.form.TodoListForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoListService {

    private final TodoListDaoInterface todoListDao;

    @Autowired
    public TodoListService(TodoListDaoInterface todoListDao) {
        this.todoListDao = todoListDao;
    }

    public TodoList createOneForCustomer(Customer customer, TodoListForm todoListForm) throws EntityAlreadyExistException {
        String title = todoListForm.getTitle();
        if (todoListExists(customer, title)) throw new EntityAlreadyExistException("There is already a todo-list with name: " + title + ".");
        TodoList todoList = new TodoList(title);
        todoList.setCustomer(customer);
        todoList.setNum(todoListDao.countByCustomer(customer) + 1);
        return todoListDao.save(todoList);
    }

    public List<TodoList> findAllByCustomer(Customer customer) {
        return todoListDao.findAllByCustomer(customer);
    }

    public TodoList getOneByCustomerAndNum(Customer customer, int todoListNum) {
        TodoList todoList = todoListDao.findByCustomerAndNum(customer, todoListNum);
        if (todoList == null) throw new EntityNotFoundException("There is no todo-list with num = " + todoListNum);
        return todoList;
    }

    public TodoList updateOneForCustomer(Customer customer, TodoListForm todoListForm) throws EntityAlreadyExistException {
        String title = todoListForm.getTitle();
        int todoListNum = todoListForm.getNum();
        if (todoListExists(customer, title)) throw new EntityAlreadyExistException("There is already a todo-list with name: " + title + ".");
        TodoList todoList = todoListDao.findByCustomerAndNum(customer, todoListNum);
        if (todoList == null) throw new EntityNotFoundException("There is no todo-list with num = " + todoListNum);
        todoList.setTitle(title);
        return todoListDao.save(todoList);
    }

    public void deleteOneForCustomer(Customer customer, int todoListNum) {
        TodoList todoList = todoListDao.findByCustomerAndNum(customer, todoListNum);
        if (todoList == null) throw new EntityNotFoundException("There is no todo-list with num = " + todoListNum);
        todoListDao.deleteById(todoList.getId());
        computeTodoListNum(customer);
    }

    private boolean todoListExists(Customer customer, String title) {
        return todoListDao.findByCustomerAndTitle(customer, title) != null;
    }

    private void computeTodoListNum(Customer customer) {
        int num = 0;
        List<TodoList> todoLists = todoListDao.findAllByCustomer(customer);
        for (TodoList todoList : todoLists) {
            todoList.setNum(++num);
            todoListDao.save(todoList);
        }
    }

}
