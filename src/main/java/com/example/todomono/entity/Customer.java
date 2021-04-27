package com.example.todomono.entity;

import com.example.todomono.form.CustomerUpdateForm;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(unique=true)
    private String name;

    @NotNull
    private String encodedPassword;

    @NotNull
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "customer_role", joinColumns = { @JoinColumn(name = "customer_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private final Set<Role> roleSet = new HashSet<>();

    public Customer() {
    }

    public Customer(String name, String encodedPassword) {
        this.name = name;
        this.encodedPassword = encodedPassword;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String password) {
        this.encodedPassword = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoleSet() {
        return roleSet;
    }

    public void addRole(Role role) {
        roleSet.add(role);
        role.getCustomerSet().add(this);
    }

    public void clearRoles() {
        roleSet.forEach(role -> role.getCustomerSet().remove(this));
        roleSet.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return (id == customer.id) && (name.equals(customer.name)) && (encodedPassword.equals(customer.encodedPassword)) && Objects.equals(roleSet, customer.roleSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, encodedPassword);
    }

    public CustomerUpdateForm convertToCustomerUpdateForm() {
        return new CustomerUpdateForm(id, name, enabled);
    }

}
