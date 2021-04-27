package com.example.todomono.dao.memory;

import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.entity.Role;
import com.example.todomono.exception.DaoConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleDaoMemoryTest {

    private final RoleDaoInterface roleDao = new RoleDaoMemory();

    @Test
    void save() {
        String roleName = "USER";
        Role role = new Role(roleName);
        roleDao.save(role);
        assertThat(role.getId(), equalTo(getLastId()));
        assertThat(role.getRoleName(), equalTo(roleName));
    }

    @Test
    void findById() {
        Role role = new Role("USER");
        roleDao.save(role);
        assertThat(roleDao.findById(role.getId()), equalTo(role));
    }

    @Test
    void findAll() {
        roleDao.save(new Role("USER"));
        roleDao.save(new Role("ADMIN"));
        assertThat(roleDao.findAll(), hasSize(2));
    }

    @Test
    void save_whenRoleNameAlreadyExist_thenThrowException() {
        roleDao.save(new Role("USER"));
        roleDao.save(new Role("ADMIN"));
        assertThat(roleDao.findAll(), hasSize(2));
        assertThatThrownBy(() -> roleDao.save(new Role("ADMIN")))
                .isInstanceOf(DaoConstraintViolationException.class);
    }

    @Test
    void update() {
        Role role = new Role("ADMIN");
        roleDao.save(role);
        long id = role.getId();
        Role savedRole = roleDao.findById(id);
        savedRole.setRoleName("USER");
        roleDao.save(savedRole);
        assertEquals("USER", roleDao.findById(id).getRoleName());
    }

    private long getLastId() {
        List<Role> roleSet = roleDao.findAll();
        long lastId = 0;
        for (Role role : roleSet) {
            lastId = Math.max(lastId, role.getId());
        }
        return lastId;
    }

}