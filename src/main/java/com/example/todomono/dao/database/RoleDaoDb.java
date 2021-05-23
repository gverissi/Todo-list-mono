package com.example.todomono.dao.database;

import com.example.todomono.dao.RoleDaoInterface;
import com.example.todomono.entity.Role;
import com.example.todomono.repository.RoleRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"prod", "dev"})
public class RoleDaoDb extends EntityDaoDb<Role, RoleRepositoryInterface> implements RoleDaoInterface {

    private final RoleRepositoryInterface roleRepository;

    @Autowired
    public RoleDaoDb(RoleRepositoryInterface roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

}
