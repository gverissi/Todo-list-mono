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

    /**
     * Create a TodoList associated to a Customer. If the Customer already contains a TodoList with the same title
     * then a EntityAlreadyExistException is thrown.
     * @param customer Parent of the TodoList.
     * @param todoListForm Dto that must contain a unique tile
     * @return The created TodoList entity.
     * @throws EntityAlreadyExistException If TodoList title is not unique.
     */
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

    /**
     * Find all TodoList own by a Customer. The TodoLists are ordered by num asc.
     * @param customer Parent of the TodoList.
     * @return List of TodoList.
     */
    public List<TodoList> findAllByCustomer(Customer customer) {
        return todoListDao.findAllByCustomer(customer);
    }

    /**
     * Find one TodoList according to it's num and it's parent Customer.
     * If the TodoList doesn't exit then Throw an DaoEntityNotFoundException that extend RuntimeException and the 404 error page is displayed.
     * @param customer Parent of the TodoList.
     * @param todoListNum Num of the TodoList.
     * @return The TodoList entity.
     */
    public TodoList findOneByCustomerAndNum(Customer customer, int todoListNum) {
        return todoListDao.findByCustomerAndNum(customer, todoListNum);
    }

    /**
     * Update a TodoList owned by a Customer, according to a TodoListForm Dto.
     * If the Todo doesn't exist Throw an DaoEntityNotFoundException and thus the 404 error page is displayed.
     * @param customer Parent of the TodoList.
     * @param todoListForm Dto containing the modifications.
     * @return The modified TodoList entity.
     * @throws EntityAlreadyExistException If the modified title already exist.
     */
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

    /**
     * Delete a TodoList.
     * If the TodoList doesn't exist Throw an DaoEntityNotFoundException and thus the 404 error page is displayed.
     * @param customer Parent of the TodoList.
     * @param todoListNum Num of the TodoList.
     */
    public void deleteOneForCustomer(Customer customer, int todoListNum) {
        TodoList todoList = todoListDao.findByCustomerAndNum(customer, todoListNum);
        todoListDao.deleteById(todoList.getId());
        computeTodoListNum(customer);
    }

    /**
     * Compute all TodoLists num. This method must be called after deleting a Todo.
     * This way the nums is a continuous series starting at 1.
     * @param customer Parent of the TodoLists.
     */
    private void computeTodoListNum(Customer customer) {
        int num = 0;
        List<TodoList> todoLists = todoListDao.findAllByCustomer(customer);
        for (TodoList todoList : todoLists) {
            todoList.setNum(++num);
            todoListDao.save(todoList);
        }
    }

}
