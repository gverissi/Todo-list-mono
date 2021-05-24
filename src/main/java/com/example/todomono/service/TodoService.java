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

    /**
     * Create a Todo associated to a TodoList. If the TodoList already contains a Todo with the same label
     * then a EntityAlreadyExistException is thrown.
     * @param todoList The parent of the Todo.
     * @param todoForm The Todo dto witch must contain a unique label.
     * @return The created Todo entity.
     * @throws EntityAlreadyExistException If Todo label is not unique.
     */
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

    /**
     * Find all Todos own by a TodoList. The Todos are ordered by num asc.
     * @param todoList Parent of the Todos.
     * @return List of Todo.
     */
    public List<Todo> findAllByTodoList(TodoList todoList) {
        return todoDao.findAllByTodoList(todoList);
    }

    /**
     * Find one Todo according to it's num and it's parent TodoList.
     * If the Todo doesn't exit then Throw an DaoEntityNotFoundException that extend RuntimeException and the 404 error page is displayed.
     * @param todoList Parent of the Todo.
     * @param todoNum Num of the Todo.
     * @return The Todo entity.
     */
    public Todo findOneByTodoListAndNum(TodoList todoList, int todoNum) {
        return todoDao.findByTodoListAndNum(todoList, todoNum);
    }

    /**
     * Update a Todo owned by a TodoList, according to a TodoForm Dto.
     * If the Todo doesn't exist Throw an DaoEntityNotFoundException and thus the 404 error page is displayed.
     * @param todoList Parent of the Todo.
     * @param todoForm Dto containing the modifications.
     * @return The modified Todo entity.
     * @throws EntityAlreadyExistException If the modified label already exist.
     */
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

    /**
     * Delete a Todo.
     * If the Todo doesn't exist Throw an DaoEntityNotFoundException and thus the 404 error page is displayed.
     * @param todoList Parent of the Todo.
     * @param todoNum Num of the Todo.
     */
    public void deleteOneForTodoList(TodoList todoList, int todoNum) {
        Todo todo = todoDao.findByTodoListAndNum(todoList, todoNum);
        todoDao.deleteById(todo.getId());
        computeTodoNum(todoList);
    }

    /**
     * Compute all Todos num. This method must be called after deleting a Todo. This way the nums is a continuous series starting at 1.
     * @param todoList Parent of the Todos.
     */
    private void computeTodoNum(TodoList todoList) {
        int num = 0;
        List<Todo> todos = todoDao.findAllByTodoList(todoList);
        for (Todo todo : todos) {
            todo.setNum(++num);
            todoDao.save(todo);
        }
    }

}
