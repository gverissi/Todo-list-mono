package com.example.todomono.repository;

import com.example.todomono.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepositoryInterface extends JpaRepository<Role, Long> {

    Role findByRoleName(String roleName);

}
