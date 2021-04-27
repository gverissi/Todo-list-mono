package com.example.todomono.service;

import com.example.todomono.dao.TodoListDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.DaoConstraintViolationException;
import com.example.todomono.exception.EntityAlreadyExistException;
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
        TodoList todoList = new TodoList(title);
        todoList.setCustomer(customer);
        todoList.setNum(todoListDao.countByCustomer(customer) + 1);
        try {
            return todoListDao.save(todoList);
        } catch (DaoConstraintViolationException e) {
            throw new EntityAlreadyExistException("There is already a todo-list with name: " + title);
        }
    }

    public List<TodoList> findAllByCustomer(Customer customer) {
        return todoListDao.findAllByCustomer(customer);
    }

    public TodoList findOneByCustomerAndNum(Customer customer, int todoListNum) {
        return todoListDao.findByCustomerAndNum(customer, todoListNum);
    }

    public TodoList updateOneForCustomer(Customer customer, TodoListForm todoListForm) throws EntityAlreadyExistException {
        String title = todoListForm.getTitle();
        int todoListNum = todoListForm.getNum();
        TodoList todoList = todoListDao.findByCustomerAndNum(customer, todoListNum);
        todoList.setTitle(title);
        todoList.setFinished(todoListForm.isFinished());
        try {
            return todoListDao.save(todoList);
        } catch (DaoConstraintViolationException e) {
            throw new EntityAlreadyExistException("There is already a todo-list with name: " + title);
        }
    }

    public void deleteOneForCustomer(Customer customer, int todoListNum) {
        TodoList todoList = todoListDao.findByCustomerAndNum(customer, todoListNum);
        todoListDao.deleteById(todoList.getId());
        computeTodoListNum(customer);
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
