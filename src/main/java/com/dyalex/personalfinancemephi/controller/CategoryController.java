package com.dyalex.personalfinancemephi.controller;

import com.dyalex.personalfinancemephi.model.Category;
import com.dyalex.personalfinancemephi.model.TransactionType;
import com.dyalex.personalfinancemephi.model.User;
import com.dyalex.personalfinancemephi.model.exception.CategoryExistsException;
import com.dyalex.personalfinancemephi.service.CategoryService;
import com.dyalex.personalfinancemephi.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@AllArgsConstructor
@Controller
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping("/addCategory")
    public String showAddCategoryPage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Category category = new Category();
        category.setUser(user);
        model.addAttribute("category", category);
        model.addAttribute("transactionTypes", TransactionType.values());
        return "add-category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute("category") @Valid Category category) throws CategoryExistsException {
        categoryService.saveCategory(category);
        return "redirect:/";
    }

    @GetMapping("/editCategory/{id}")
    public String showUpdateCategoryPage(@PathVariable("id") long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        model.addAttribute("transactionTypes", TransactionType.values());
        return "update-category";
    }

    @PostMapping("/updateCategory/{id}")
    public String updateCategory(@PathVariable("id") long id, @ModelAttribute("category") @Valid Category category) throws CategoryExistsException {
        categoryService.updateCategory(id, category);
        return "redirect:/";
    }

    @DeleteMapping("/deleteCategory/{id}")
    public String deleteExpense(@PathVariable("id") long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/";
    }
}
