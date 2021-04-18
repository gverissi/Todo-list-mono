package com.example.todomono.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ChangeCustomerNameForm {

    @NotNull
    @NotEmpty(message = "Field required")
    private String name;

    @NotNull
    @NotEmpty(message = "Field required")
    private String password;

    public ChangeCustomerNameForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
