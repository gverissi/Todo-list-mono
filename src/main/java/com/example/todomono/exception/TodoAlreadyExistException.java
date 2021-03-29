package com.example.todomono.exception;

public class TodoAlreadyExistException extends Exception{

    public TodoAlreadyExistException(String message) {
        super(message);
    }

}
