package com.dyalex.personalfinancemephi.controller;

import com.dyalex.personalfinancemephi.model.*;
import com.dyalex.personalfinancemephi.service.FinanceService;
import com.dyalex.personalfinancemephi.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
public class FinanceController {
    private final FinanceService financeService;
    private final UserService userService;

    @GetMapping("/")
    public String viewHomePage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        List<Wallet> wallets = financeService.getAllWallets(user.getId());
        List<Category> categories = financeService.getAllCategories(user.getId());
        List<Transaction> transactions = financeService.getAllTransactions(user.getId());

        model.addAllAttributes(getModelAttributes(wallets, categories, transactions));

        return "index";
    }

    @PostMapping("/display")
    public String viewFilteredHomePage(@ModelAttribute @Valid FinanceFilter financeFilter,
                                       Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        List<Wallet> wallets = financeService.getWalletsByIds(financeFilter.getWallets().stream()
                .map(Wallet::getId).collect(Collectors.toList()), user.getId());
        List<Category> categories = financeService.getCategoriesByIds(financeFilter.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList()), user.getId());
        List<Transaction> transactions = wallets.stream().flatMap(wallet -> wallet.getTransactions().stream()
                .filter(transaction -> categories.contains(transaction.getCategory())))
                .toList();

        model.addAllAttributes(getModelAttributes(wallets, categories, transactions));
        return "index";
    }

    private List<Transaction> filterTransactions(List<Transaction> transactions, TransactionType filter) {
        return transactions.stream()
                .filter(transaction -> transaction.getCategory().getType() == filter)
                .collect(Collectors.toList());
    }

    private BigDecimal getTotal(List<Transaction> transactions) {
        return transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<String, ?> getModelAttributes(List<Wallet> wallets, List<Category> categories,
                                              List<Transaction> transactions) {
        Map<String, Object> modelAttributes = new HashMap<>();
        List<Transaction> expenses = filterTransactions(transactions, TransactionType.EXPENSE);
        List<Transaction> income = filterTransactions(transactions, TransactionType.INCOME);
        List<Transaction> other = filterTransactions(transactions, TransactionType.OTHER);

        BigDecimal balance = wallets.stream().map(Wallet::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalSpent = getTotal(expenses);
        BigDecimal totalIncome = getTotal(income);

        FinanceFilter financeFilter = FinanceFilter.builder()
                .wallets(wallets)
                .categories(categories)
                .build();

        modelAttributes.put("financeFilter", financeFilter);
        modelAttributes.put("wallets", wallets);
        modelAttributes.put("categories", categories);
        modelAttributes.put("expenses", expenses);
        modelAttributes.put("income", income);
        modelAttributes.put("otherTransactions", other);
        modelAttributes.put("balance", balance);
        modelAttributes.put("totalSpent", totalSpent);
        modelAttributes.put("totalIncome", totalIncome);
        return modelAttributes;
    }
}
