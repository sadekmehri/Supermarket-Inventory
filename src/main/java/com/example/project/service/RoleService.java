package com.example.project.service;

import com.example.project.entity.Role;
import com.example.project.exception.ApiRequestException;
import com.example.project.repository.RoleRepository;
import com.example.project.utility.ListUtils;
import com.example.project.utility.OptionalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /* Get list of roles */
    public List<Role> getRoles() {
        List<Role> lstRoles = roleRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        ListUtils.isListEmpty(lstRoles);

        return lstRoles;
    }

    /* Get specific role by id */
    public Role getRole(int id) {
        return roleRepository.findById(id).orElseThrow(() -> {
            throw new ApiRequestException("Role with id " + id + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    /* Create new role */
    public Role createRole(Role role) {
        isRoleExistByName(role.getName());
        return roleRepository.save(role);
    }

    /* Update specific role */
    public Role updateRole(int id, Role newRole) {
        Role oldRole = getRole(id);

        isRoleNameExistWhenUpdating(id, newRole.getName());
        oldRole.setName(newRole.getName());

        return roleRepository.save(oldRole);
    }

    /* Delete specific role */
    public void deleteRole(int id) {
        isRoleExistById(id);

        roleRepository.deleteById(id);
    }

    /*
     *
     *
     * Utils
     *
     *
     * */

    /* Check if the role exist filtering by id */
    private void isRoleExistById(int roleId) {
        boolean isExist = roleRepository.existsById(roleId);
        if (!isExist) throw new ApiRequestException("Role with id " + roleId + " does not exist", HttpStatus.NOT_FOUND);
    }

    /* Check if the role exists with the given name */
    private void isRoleExistByName(String roleName) {
        Optional<Role> role = roleRepository.findRoleByName(roleName);
        OptionalUtils.isPresent(role, "Role with name " + roleName + " already exists");
    }

    /* Check if the role exists with the given name when updating the role name */
    private void isRoleNameExistWhenUpdating(int id, String roleName) {
        Optional<Role> role = roleRepository.findCategoryByNameToUpdate(id, roleName);
        OptionalUtils.isPresent(role, "Role with name " + roleName + " already exists");
    }

}
