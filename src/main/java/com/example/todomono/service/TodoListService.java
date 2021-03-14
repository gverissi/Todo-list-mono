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
import java.util.stream.Collectors;

@Service
public class TodoListService {

    @Autowired
    private final TodoListDaoInterface todoListDao;

    @Autowired
    private final CustomerDaoInterface customerDao;

    @Autowired
    private final AuthenticationFacadeInterface authenticationFacade;


    public TodoListService(TodoListDaoInterface todoListDao, CustomerDaoInterface customerDao, AuthenticationFacadeInterface authenticationFacade) {
        this.todoListDao = todoListDao;
        this.customerDao = customerDao;
        this.authenticationFacade = authenticationFacade;
    }

    public List<TodoList> findAllByCustomer() {
        return todoListDao.findAllByCustomer(getCustomer());
    }

    public TodoList createOne(TodoListForm todoListForm) throws TodoListAlreadyExistException {
        Customer customer = getCustomer();
        String title = todoListForm.getTitle();
        if (todoListExists(customer, title)) throw new TodoListAlreadyExistException("There is already a todo-list with name: " + title + ".");
        TodoList todoList = new TodoList(title);
        todoList.setCustomer(customer);
        todoList.setNum(todoListDao.countByCustomer(customer) + 1);
        return todoListDao.save(todoList);
    }

    public TodoList getOneByNum(long todoListNum) {
        TodoList todoList = todoListDao.findByCustomerAndNum(getCustomer(), todoListNum);
        if (todoList == null) throw new EntityNotFoundException("There is no todo-list with num = " + todoListNum);
        return todoList;
    }

    public TodoList updateOne(TodoListForm todoListForm) throws TodoListAlreadyExistException {
        Customer customer = getCustomer();
        String title = todoListForm.getTitle();
        long todoListNum = todoListForm.getNum();
        if (todoListExists(customer, title)) throw new TodoListAlreadyExistException("There is already a todo-list with name: " + title + ".");
        TodoList todoList = todoListDao.findByCustomerAndNum(customer, todoListNum);
        if (todoList == null) throw new EntityNotFoundException("There is no todo-list with num = " + todoListNum);
        todoList.setTitle(title);
        return todoListDao.save(todoList);
    }

    public void deleteOne(long todoListNum) {
        TodoList todoList = todoListDao.findByCustomerAndNum(getCustomer(), todoListNum);
        if (todoList == null) throw new EntityNotFoundException("There is no todo-list with num = " + todoListNum);
        todoListDao.delete(todoList);
        computeTodoListNum();
    }

    private Customer getCustomer() {
        String customerName = authenticationFacade.getAuthentication().getName();
        return customerDao.findByName(customerName);
    }

    private boolean todoListExists(Customer customer, String title) {
        return todoListDao.findByCustomerAndTitle(customer, title) != null;
    }

    private void computeTodoListNum() {
        long num = 0L;
        List<TodoList> todoLists = todoListDao.findAllByCustomer(getCustomer());
        for (TodoList todoList : todoLists) {
            todoList.setNum(++num);
            todoListDao.save(todoList);
        }
    }

}
