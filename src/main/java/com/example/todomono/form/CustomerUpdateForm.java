package com.example.todomono.form;

import java.util.*;

public class CustomerUpdateForm {

    private int id;

    private String name;

    private boolean enabled;

    private List<RoleUpdateForm> roles = new ArrayList<>();

    public CustomerUpdateForm() {
    }

    public CustomerUpdateForm(int id, String name, boolean enabled) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<RoleUpdateForm> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleUpdateForm> roles) {
        this.roles = roles;
    }

}
