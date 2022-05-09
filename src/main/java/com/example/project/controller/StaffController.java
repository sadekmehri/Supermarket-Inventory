package com.example.project.controller;

import com.example.project.entity.Staff;
import com.example.project.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path = "/staffs")
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    // @desc      Get list of staff records
    // @route     GET /staffs
    // @sort      String[] sort = Asc,Desc
    // @paginate  page,size = ?page=&size= - ?size= - ?page= - ?sort=id,desc
    // @access    Private User - Administrator

    @PreAuthorize("hasRole('Administrator')")
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getStaffs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        return staffService.getStaffs(page, size, sort);
    }

    // @desc      Get list of orders records by staff
    // @route     GET /staffs/{id}/orders
    // @sort      String[] sort = Asc,Desc
    // @paginate  page,size = ?page=&size= - ?size= - ?page= - ?sort=id,desc
    // @access    Private User - Administrator

    @PreAuthorize("hasRole('Administrator')")
    @GetMapping(path = "{id}/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getOrdersByStaff(
            @PathVariable(value = "id") int staffId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "staffId,desc") String[] sort) {

        return staffService.getOrdersByStaff(staffId, page, size, sort);
    }

    // @desc    Get specific staff by id
    // @route   GET /staff/{id}
    // @access  Private User - Administrator

    @PreAuthorize("hasRole('Administrator')")
    @GetMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Staff getEmployee(@PathVariable("id") int id) {
        return staffService.getStaff(id);
    }

    // @desc      Create new staff records
    // @route     POST /staffs
    // @access    Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Staff createEmployee(@Valid @RequestBody Staff staff) {
        return staffService.createStaff(staff);
    }

    // @desc    Update existing staff
    // @route   PUT /staff/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Staff updateEmployee(@PathVariable("id") int id, @Valid @RequestBody Staff staff) {
        return staffService.updateStaff(id, staff);
    }

    // @desc    Delete existing staff
    // @route   DELETE /staffs/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @DeleteMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable("id") int id) {
        staffService.deleteStaff(id);
    }

}
