package com.example.todomono.dao;

import com.example.todomono.entity.Todo;
import com.example.todomono.entity.TodoList;
import com.example.todomono.exception.DaoEntityNotFoundException;

import java.util.List;

public interface TodoDaoInterface extends EntityDaoInterface<Todo> {

    /**
     * Find all Todos owned by a TodoList.
     * @param todoList Parent of the Todos.
     * @return List of Todos.
     */
    List<Todo> findAllByTodoList(TodoList todoList);

    /**
     * Find one Todo according to it's label.
     * @param todoList Parent of the Todo.
     * @param label The label of the Todo.
     * @return The Todo entity.
     */
    Todo findByTodoListAndLabel(TodoList todoList, String label);

    /**
     * Find one Todo according to it's num.
     * If the Todo doesn't exist Throw an DaoEntityNotFoundException and thus the 404 error page is displayed.
     * @param todoList Parent of the Todo.
     * @param num The num of the Todo.
     * @return The Todo entity.
     * @throws DaoEntityNotFoundException If the Todo doesn't exist.
     */
    Todo findByTodoListAndNum(TodoList todoList, int num) throws DaoEntityNotFoundException;

    /**
     * Count all Todos of a TodoList.
     * @param todoList Parent of the Todos.
     * @return Number of Todos of a TodoList.
     */
    int countByTodoList(TodoList todoList);

}
