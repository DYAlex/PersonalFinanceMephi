package com.dyalex.personalfinancemephi.service;

import com.dyalex.personalfinancemephi.model.Category;
import com.dyalex.personalfinancemephi.model.TransactionType;
import com.dyalex.personalfinancemephi.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public List<Category> getAllCategories(Long id) {
        return categoryRepository.findAllByUserId(id);
    }

    public List<Category> getIncomeCategories(Long id) {
        return categoryRepository.findAllByUserIdAndType(id, TransactionType.INCOME);
    }

    public List<Category> getExpenseCategories(Long id) {
        return categoryRepository.findAllByUserIdAndType(id, TransactionType.EXPENSE);
    }

    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void updateCategory(long id, Category category) {
        Category oldCategory = categoryRepository.findById(id).orElse(null);
        if (oldCategory != null) {
            Category newCategory = Category.builder()
                    .id(oldCategory.getId())
                    .user(oldCategory.getUser())
                    .name(category.getName())
                    .type(category.getType())
                    .build();
            categoryRepository.save(newCategory);
        }
    }

    public void deleteCategoryById(long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> getCategoriesByIds(Long userId, List<Long> categoryIds) {
        return categoryRepository.findAllByUserIdAndIdIsIn(userId, categoryIds);
    }
}
