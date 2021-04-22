package com.example.todomono.form;

import com.example.todomono.validation.PasswordMatches;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@PasswordMatches
public class CustomerCreateForm {

    @NotNull
    @NotEmpty(message = "Field required")
    private String name;

    @NotNull
    @NotEmpty(message = "Field required")
    private String password;

    @NotNull
    @NotEmpty(message = "Field required")
    private String matchingPassword;

    public CustomerCreateForm() {
    }

    public CustomerCreateForm(@NotNull @NotEmpty(message = "Field required") String name, @NotNull @NotEmpty(message = "Field required") String password, @NotNull @NotEmpty(message = "Field required") String matchingPassword) {
        this.name = name;
        this.password = password;
        this.matchingPassword = matchingPassword;
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

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

}
