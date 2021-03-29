package com.example.todomono.entity;

import com.example.todomono.form.TodoForm;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String label;

    private long num;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "todoList_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TodoList todoList;

    public Todo() {
    }

    public Todo(@NotNull String title) {
        this.label = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    public void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }

    public TodoForm convertToDto() {
        TodoForm todoForm = new TodoForm(label);
        todoForm.setNum(num);
        return todoForm;
    }
}
