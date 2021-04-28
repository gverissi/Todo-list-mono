package com.example.todomono.dao.memory;

import com.example.todomono.dao.TodoListDaoInterface;
import com.example.todomono.entity.Customer;
import com.example.todomono.entity.TodoList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

class TodoListDaoMemoryTest {

    TodoListDaoInterface todoListDao;
    Customer customer;
    TodoList todoList1;
    TodoList todoList2;

    @BeforeEach
    void init() {
        todoListDao = new TodoListDaoMemory();
        customer = new Customer("greg", "aBcD");
        todoList1 = new TodoList("title 1");
        todoList1.setNum(1);
        todoList1.setCustomer(customer);
        todoListDao.save(todoList1);
        todoList2 = new TodoList("title 2");
        todoList2.setNum(2);
        todoList2.setCustomer(customer);
        todoListDao.save(todoList2);
    }

    @Test
    void save() {
        // Given
        String title = "My todo-list";
        TodoList todoList = new TodoList(title);
        // When
        TodoList savedTodoList = todoListDao.save(todoList);
        // Then
        assertThat(todoList.getId(), not(0L));
        assertThat(savedTodoList.getTitle(), equalTo(title));
    }

    @Test
    void findAllByCustomer() {
        // When
        List<TodoList> todoLists = todoListDao.findAllByCustomer(customer);
        // Then
        assertThat(todoLists.size(), equalTo(2));
    }

    @Test
    void findByCustomerAndTitle() {
        // When
        TodoList todoList = todoListDao.findByCustomerAndTitle(customer, todoList2.getTitle());
        // Then
        assertThat(todoList, equalTo(todoList2));
    }

    @Test
    void findByCustomerAndNum() {
        // When
        TodoList todoList = todoListDao.findByCustomerAndNum(customer, todoList2.getNum());
        // Then
        assertThat(todoList, equalTo(todoList2));
    }

    @Test
    void countByCustomer() {
        // When
        int nbTodoLists = todoListDao.countByCustomer(customer);
        // Then
        assertThat(nbTodoLists, equalTo(2));
    }

}