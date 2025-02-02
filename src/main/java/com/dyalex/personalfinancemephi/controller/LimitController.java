package com.dyalex.personalfinancemephi.controller;

import com.dyalex.personalfinancemephi.model.*;
import com.dyalex.personalfinancemephi.model.exception.CategoryNotFoundException;
import com.dyalex.personalfinancemephi.model.exception.WalletNotFoundException;
import com.dyalex.personalfinancemephi.service.CategoryService;
import com.dyalex.personalfinancemephi.service.LimitService;
import com.dyalex.personalfinancemephi.service.UserService;
import com.dyalex.personalfinancemephi.service.WalletService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@AllArgsConstructor
@Controller
public class LimitController {
    private final CategoryService categoryService;
    private final LimitService limitService;
    private final UserService userService;
    private final WalletService walletService;

    @GetMapping("/addLimit/{categoryId}")
    public String showAddLimitPage(@PathVariable String categoryId, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Category> categories = categoryService.getAllCategories(user.getId());
        List<Wallet> wallets = walletService.getAllWallets(user.getId());
        Limit limit = new Limit();
        limit.setCategory(categories.stream()
                .filter(category -> Objects.equals(category.getId().toString(), categoryId)).findFirst().orElse(null));
        model.addAttribute("limit", limit);
        model.addAttribute("wallets", wallets);
        model.addAttribute("categories", categories);
        return "add-limit";
    }

    @PostMapping("/saveLimit")
    public String saveLimit(@ModelAttribute("limit") @Valid Limit limit, Model model, Principal principal)
            throws CategoryNotFoundException, WalletNotFoundException {
        if (limit.getWallet() == null) {
            model.addAttribute("walletError", "You must select a wallet first");
            model.addAllAttributes(getModelAttributes(limit, principal));
            return "add-limit";
        }
        if (limit.getCategory() == null) {
            model.addAttribute("categoryError", "You must select a category first");
            model.addAllAttributes(getModelAttributes(limit, principal));
            return "add-limit";
        }

        Optional<Limit> limitFromDb = limitService.getLimitByWalletAndCategory(limit.getWallet().getId(), limit.getCategory().getId());
        if (limitFromDb.isPresent()) {
            model.addAttribute("limitError", "Такой лимит уже существует");
            model.addAllAttributes(getModelAttributes(limit, principal));
            return "add-limit";
        }
        limitService.saveLimit(limit);
        return "redirect:/";
    }

    @GetMapping("/editLimit/{limitId}")
    public String showUpdateLimitPage(@PathVariable("limitId") long limitId, Model model, Principal principal) {
        Limit limit = limitService.getLimitById(limitId);
        model.addAllAttributes(getModelAttributes(limit, principal));

        return "update-limit";
    }

    @PostMapping("/updateLimit/{limitId}")
    public String updateLimit(@PathVariable("limitId") long limitId, @ModelAttribute("limit") @Valid Limit limit, Model model, Principal principal)
            throws CategoryNotFoundException, WalletNotFoundException {
        if (limit.getWallet() == null) {
            model.addAttribute("walletError", "You must select a wallet first");
            model.addAllAttributes(getModelAttributes(limit, principal));
            return "update-limit";
        }
        if (limit.getCategory() == null) {
            model.addAttribute("categoryError", "You must select a category first");
            model.addAllAttributes(getModelAttributes(limit, principal));
            return "update-limit";
        }

        Optional<Limit> limitFromDb = limitService.getLimitByWalletAndCategory(limit.getWallet().getId(), limit.getCategory().getId());
        if (limitFromDb.isPresent() && limitFromDb.get().getId() != limitId) {
            model.addAttribute("limitError", "Такой лимит уже существует");
            model.addAllAttributes(getModelAttributes(limit, principal));
            return "add-limit";
        }
        limitService.updateLimit(limitId, limit);
        return "redirect:/";
    }

    @DeleteMapping("/deleteLimit/{limitId}")
    public String deleteExpense(@PathVariable("limitId") long limitId) {
        limitService.deleteLimitById(limitId);
        return "redirect:/";
    }

    private Map<String, ?> getModelAttributes(Limit limit, Principal principal) {
        Map<String, Object> modelAttributes = new HashMap<>();

        User user = userService.findByUsername(principal.getName());
        List<Wallet> wallets = walletService.getAllWallets(user.getId());
        List<Category> categories = categoryService.getAllCategories(user.getId());

        modelAttributes.put("limit", limit);
        modelAttributes.put("wallets", wallets);
        modelAttributes.put("categories", categories);

        return modelAttributes;
    }
}
