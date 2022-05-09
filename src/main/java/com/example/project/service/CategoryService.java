package com.example.project.service;

import com.example.project.entity.Category;
import com.example.project.entity.Product;
import com.example.project.exception.ApiRequestException;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.ProductRepository;
import com.example.project.utility.Beautify;
import com.example.project.utility.ListUtils;
import com.example.project.utility.OptionalUtils;
import com.example.project.utility.SortAndPaginate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    /* Get list of products by a given category */
    public Map<String, Object> getAllProductsByCategoryId(int idCategory, int page, int size, String[] sort) {
        getCategory(idCategory);

        SortAndPaginate<Product> sp = new SortAndPaginate<>(page, size, sort);
        sp.listSortAttribute();

        List<Product> lstProducts = sp.getSortedPaginatedList(
                productRepository.findByCategoryId(idCategory, sp.getPagingSort())
        );

        return new Beautify<>(sp.getPaginator(), lstProducts).transformDataToJson();
    }

    /* Get list of categories */
    public List<Category> getCategories() {
        List<Category> lstCategories = categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        ListUtils.isListEmpty(lstCategories);

        return lstCategories;
    }

    /* Get specific category by id */
    public Category getCategory(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> {
            throw new ApiRequestException("Category with id " + id + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    /* Create new category */
    public Category createCategory(Category category) {
        isCategoryExistByName(category.getName());

        return categoryRepository.save(category);
    }

    /* Update specific category */
    public Category updateCategory(int id, Category newCategory) {
        Category oldCategory = getCategory(id);

        isProductNameExistWhenUpdating(id, newCategory.getName());
        oldCategory.setName(newCategory.getName());

        return categoryRepository.save(oldCategory);
    }

    /* Delete specific category */
    public void deleteCategory(int id) {
        boolean isExist = categoryRepository.existsById(id);
        if (!isExist) throw new ApiRequestException("Category with id " + id + " does not exist", HttpStatus.NOT_FOUND);

        categoryRepository.deleteById(id);
    }

    /*
     *
     *
     * Utils
     *
     *
     * */

    /* Check if the category exists with the given name */
    private void isCategoryExistByName(String categoryName) {
        Optional<Category> category = categoryRepository.findCategoryByName(categoryName);
        OptionalUtils.isPresent(category, "Category with name " + categoryName + " already exists");
    }

    /* Check if the category exists with the given name when updating the category name */
    private void isProductNameExistWhenUpdating(int id, String categoryName) {
        Optional<Category> category = categoryRepository.findCategoryByNameToUpdate(id, categoryName);
        OptionalUtils.isPresent(category, "Category with name " + categoryName + " already exists");
    }

}
