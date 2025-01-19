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
import java.util.List;
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
        List<Transaction> transactions = financeService.getAllTransactions(user.getId());
        List<Transaction> expenses = transactions.stream()
                .filter(transaction -> transaction.getCategory().getType() == TransactionType.EXPENSE)
                .collect(Collectors.toList());
        List<Transaction> income = transactions.stream()
                .filter(transaction -> transaction.getCategory().getType() == TransactionType.INCOME)
                .toList();
        List<Transaction> other = transactions.stream()
                .filter(transaction -> transaction.getCategory().getType() == TransactionType.OTHER)
                .toList();
        List<Category> categories = financeService.getAllCategories(user.getId());
        BigDecimal balance = wallets.stream().map(Wallet::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalSpent = expenses.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalIncome = income.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        FinanceFilter financeFilter = FinanceFilter.builder()
                .wallets(wallets)
                .categories(categories)
                .build();
        model.addAttribute("financeFilter", financeFilter);
        model.addAttribute("wallets", wallets);
        model.addAttribute("categories", categories);
        model.addAttribute("expenses", expenses);
        model.addAttribute("income", income);
        model.addAttribute("otherTransactions", other);
        model.addAttribute("balance", balance);
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("totalIncome", totalIncome);

        return "index";
    }

    @PostMapping("/display")
    public String viewFilteredHomePage(@ModelAttribute @Valid FinanceFilter financeFilter,
                                       Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        List<Wallet> wallets = financeService.getWalletsByIds(financeFilter.getWallets().stream()
                .map(Wallet::getId).collect(Collectors.toList()), user.getId());
        List<Category> categories = financeService.getCategoriesByIds(financeFilter.getCategories().stream()
                        .peek(cat -> System.out.println("Category: " + cat))
                .map(Category::getId)
                .peek(catId -> System.out.println("CategoryID: " + catId))
                .collect(Collectors.toList()), user.getId());
        List<Transaction> transactions = wallets.stream().flatMap(wallet -> wallet.getTransactions().stream()
                .filter(transaction -> categories.contains(transaction.getCategory())))
                .toList();

        List<Transaction> expenses = transactions.stream()
                .filter(transaction -> transaction.getCategory().getType() == TransactionType.EXPENSE)
                .collect(Collectors.toList());
        List<Transaction> income = transactions.stream()
                .filter(transaction -> transaction.getCategory().getType() == TransactionType.INCOME)
                .toList();
        List<Transaction> other = transactions.stream()
                .filter(transaction -> transaction.getCategory().getType() == TransactionType.OTHER)
                .toList();

        BigDecimal balance = wallets.stream().map(Wallet::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalSpent = expenses.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalIncome = income.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        FinanceFilter refreshFilter = FinanceFilter.builder()
                .wallets(wallets)
                .categories(categories)
                .build();

        model.addAttribute("financeFilter", refreshFilter);
        model.addAttribute("wallets", wallets);
        model.addAttribute("categories", categories);
        model.addAttribute("expenses", expenses);
        model.addAttribute("income", income);
        model.addAttribute("otherTransactions", other);
        model.addAttribute("balance", balance);
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("totalIncome", totalIncome);
        return "index";
    }
}
