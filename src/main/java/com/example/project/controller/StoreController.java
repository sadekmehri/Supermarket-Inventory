package com.example.project.controller;

import com.example.project.entity.Store;
import com.example.project.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/stores")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    // @desc    Get list of store records
    // @route   GET /stores
    // @access  Private Administrator

    @PreAuthorize("hasRole('Administrator')")
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Store> getStores() {
        return storeService.getStores();
    }

    // @desc    Get specific store by id
    // @route   GET /stores/{id}
    // @access  Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Store getStore(@PathVariable("id") int id) {
        return storeService.getStore(id);
    }


    // @desc      Get list of staffs records by store id
    // @route     GET /stores/{id}/staffs
    // @sort      String[] sort = Asc,Desc
    // @paginate  page,size = ?page=&size= - ?size= - ?page= - ?sort=id,desc
    // @access    Private Administrator

    @PreAuthorize("hasRole('Administrator')")
    @GetMapping(path = "{id}/staffs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getAllStaffsByStore(@PathVariable(value = "id") int storeId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "id,desc") String[] sort) {

        return storeService.getAllStaffsByStore(storeId, page, size, sort);
    }


    // @desc      Get list of orders records by store id
    // @route     GET /stores/{id}/orders
    // @sort      String[] sort = Asc,Desc
    // @paginate  page,size = ?page=&size= - ?size= - ?page= - ?sort=id,desc
    // @access    Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(path = "{id}/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getAllOrdersByStore(@PathVariable(value = "id") int storeId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "storeId,desc") String[] sort) {

        return storeService.getAllOrdersByStore(storeId, page, size, sort);
    }

    // @desc    Create new store
    // @route   POST /stores
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Store createStore(@Valid @RequestBody Store store) {
        return storeService.createStore(store);
    }

    // @desc    Update specific store
    // @route   PUT /stores/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Store updateStore(@PathVariable("id") int id, @Valid @RequestBody Store store) {
        return storeService.updateStore(id, store);
    }

    // @desc    Delete specific store by id
    // @route   DELETE /stores/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @DeleteMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStore(@PathVariable("id") int id) {
        storeService.deleteStore(id);
    }

}