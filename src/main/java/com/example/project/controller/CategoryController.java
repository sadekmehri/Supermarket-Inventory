package com.example.project.controller;

import com.example.project.entity.Category;
import com.example.project.service.CategoryService;
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
@RequestMapping(path = "/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // @desc      Get list of product records by category id
    // @route     GET /categories/{id}/products
    // @sort      String[] sort = Asc,Desc
    // @paginate  page,size = ?page=&size= - ?size= - ?page= - ?sort=id,desc
    // @access    Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(path = "{id}/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getAllProductsByCategoryId(@PathVariable(value = "id") int categoryId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "id,desc") String[] sort) {

        return categoryService.getAllProductsByCategoryId(categoryId, page, size, sort);
    }

    // @desc      Get list of category records
    // @route     GET /categories
    // @access    Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getCategories() {
        return categoryService.getCategories();
    }

    // @desc    Get specific category by id
    // @route   GET /categories/{id}
    // @access  Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Category getCategory(@PathVariable("id") int id) {
        return categoryService.getCategory(id);
    }

    // @desc    Create new category
    // @route   POST /categories
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@Valid @RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    // @desc    Update specific category
    // @route   PUT /categories/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Category updateCategory(@PathVariable("id") int id, @Valid @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    // @desc    Delete specific category by id
    // @route   DELETE /categories/{id}
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @DeleteMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("id") int id) {
        categoryService.deleteCategory(id);
    }

}
