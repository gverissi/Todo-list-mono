package com.example.todomono.form;

import com.example.todomono.validation.PasswordMatches;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@PasswordMatches
public class CustomerChangePasswordForm {

    @NotNull
    @NotEmpty(message = "Field required")
    private String oldPassword;

    @NotNull
    @NotEmpty(message = "Field required")
    private String password;

    @NotNull
    @NotEmpty(message = "Field required")
    private String matchingPassword;

    public CustomerChangePasswordForm() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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
