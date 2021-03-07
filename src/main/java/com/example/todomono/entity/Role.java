package com.example.todomono.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(unique = true)
    private String roleName;

    @ManyToMany(mappedBy = "roleSet")
    private final Set<Customer> customerSet = new HashSet<>();

    public Role() {
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + roleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<Customer> getCustomerSet() {
        return customerSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return (id == role.id) && roleName.equals(role.roleName) && Objects.equals(customerSet, role.customerSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName);
    }

}
