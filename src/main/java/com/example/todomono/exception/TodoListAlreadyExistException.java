package com.example.todomono.exception;

public class TodoListAlreadyExistException extends Exception{

    public TodoListAlreadyExistException(String message) {
        super(message);
    }

}
