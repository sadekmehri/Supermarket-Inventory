package com.example.project.controller;

import com.example.project.entity.Brand;
import com.example.project.service.BrandService;
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
@RequestMapping(path = "/brands")
public class BrandController {

    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    // @desc      Get list of product records by brand id
    // @route     GET /brands/{id}/products
    // @sort      String[] sort = Asc,Desc
    // @paginate  page,size = ?page=&size= - ?size= - ?page= - ?sort=id,desc
    // @access    Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(path = "{id}/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getAllProductsByBrandId(@PathVariable(value = "id") int brandId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "id,desc") String[] sort) {

        return brandService.getAllProductsByBrandId(brandId, page, size, sort);
    }

    // @desc    Get list of brand records
    // @route   GET /brands
    // @access  Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Brand> getBrands() {
        return brandService.getBrands();
    }

    // @desc    Get specific brand by id
    // @route   GET /brands/{id}
    // @access  Private User - Administrator

    @PreAuthorize("hasRole('User') or hasRole('Administrator')")
    @GetMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Brand getBrand(@PathVariable("id") int id) {
        return brandService.getBrand(id);
    }

    // @desc    Create new brand
    // @route   POST /brands
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Brand createBrand(@Valid @ModelAttribute Brand brand) {
        return brandService.createBrand(brand);
    }

    // @desc    Update existing brand
    // @route   PUT /brands
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @PutMapping(path = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Brand updateBrand(@PathVariable("id") int id, @Valid @ModelAttribute Brand brand) {
        return brandService.updateBrand(id, brand);
    }

    // @desc    Delete existing brand
    // @route   DELETE /brands/id
    // @access  Private Administrator

    @Transactional
    @PreAuthorize("hasRole('Administrator')")
    @DeleteMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBrand(@PathVariable("id") int id) {
        brandService.deleteBrand(id);
    }

}
