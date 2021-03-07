package com.example.todomono.exception;

public class CustomerAlreadyExistException extends RuntimeException{

    public CustomerAlreadyExistException(String message) {
        super(message);
    }

}
