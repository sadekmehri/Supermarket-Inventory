package com.example.project.controller;

import com.example.project.entity.Role;
import com.example.project.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // @desc    Get list of role records
    // @route   GET /roles
    // @access  Private Administrator

    @PreAuthorize("hasRole('Administrator')")
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    // @desc    Get specific role by id
    // @route   GET /roles/{id}
    // @access  Private Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Role getRole(@PathVariable("id") int id) {
        return roleService.getRole(id);
    }

    // @desc    Create new role
    // @route   POST /roles
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Role createRole(@Valid @RequestBody Role role) {
        return roleService.createRole(role);
    }

    // @desc    Update specific category
    // @route   PUT /roles/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Role updateRole(@PathVariable("id") int id, @Valid @RequestBody Role role) {
        return roleService.updateRole(id, role);
    }

    // @desc    Delete specific role by id
    // @route   DELETE /roles/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @DeleteMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable("id") int id) {
        roleService.deleteRole(id);
    }
}
