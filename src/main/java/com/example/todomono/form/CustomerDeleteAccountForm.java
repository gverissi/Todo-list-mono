package com.example.todomono.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CustomerDeleteAccountForm {

    @NotNull
    @NotEmpty(message = "Field required")
    private String password;

    public CustomerDeleteAccountForm() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
