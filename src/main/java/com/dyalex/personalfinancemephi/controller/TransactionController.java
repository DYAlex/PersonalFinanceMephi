package com.dyalex.personalfinancemephi.controller;

import com.dyalex.personalfinancemephi.model.*;
import com.dyalex.personalfinancemephi.model.exception.CategoryNotFoundException;
import com.dyalex.personalfinancemephi.model.exception.TransactionIncorrectException;
import com.dyalex.personalfinancemephi.model.exception.WalletNotFoundException;
import com.dyalex.personalfinancemephi.service.CategoryService;
import com.dyalex.personalfinancemephi.service.TransactionService;
import com.dyalex.personalfinancemephi.service.UserService;
import com.dyalex.personalfinancemephi.service.WalletService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Controller
public class TransactionController {
    private final CategoryService categoryService;
    private final TransactionService transactionService;
    private final UserService userService;
    private final WalletService walletService;

    @GetMapping("/addIncome")
    public String showAddIncomePage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Category> categories = categoryService.getIncomeCategories(user.getId());
        List<Wallet> wallets = walletService.getAllWallets(user.getId());
        Transaction income = new Transaction();
        model.addAttribute("income", income);
        model.addAttribute("wallets", wallets);
        model.addAttribute("categories", categories);
        return "add-income";
    }

    @PostMapping("/saveIncome")
    public String saveIncome(@ModelAttribute("income") Transaction income, Model model, Principal principal)
            throws WalletNotFoundException, CategoryNotFoundException, TransactionIncorrectException {
        if (income.getWallet() == null || income.getCategory() == null) {
            User user = userService.findByUsername(principal.getName());
            List<Category> categories = categoryService.getIncomeCategories(user.getId());
            List<Wallet> wallets = walletService.getAllWallets(user.getId());
            if (income.getWallet() == null) {
                model.addAttribute("walletError", "You must select a wallet first");
                model.addAttribute("income", income);
                model.addAttribute("wallets", wallets);
                model.addAttribute("categories", categories);
                return "add-income";
            }
            if (income.getCategory() == null) {
                model.addAttribute("categoryError", "You must select a category first");
                model.addAttribute("income", income);
                model.addAttribute("wallets", wallets);
                model.addAttribute("categories", categories);
                return "add-income";
            }
        }

        transactionService.saveIncome(income);
        return "redirect:/";
    }

    @GetMapping("/addExpense")
    public String showAddExpensePage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Category> categories = categoryService.getExpenseCategories(user.getId());
        List<Wallet> wallets = walletService.getAllWallets(user.getId());

        Transaction expense = new Transaction();
        model.addAttribute("expense", expense);
        model.addAttribute("wallets", wallets);
        model.addAttribute("categories", categories);
        return "add-expense";
    }

    @PostMapping("/saveExpense")
    public String saveExpense(@ModelAttribute("expense") @Valid Transaction expense,
                              @ModelAttribute("warning") String warning, Model model, Principal principal)
            throws WalletNotFoundException, CategoryNotFoundException, TransactionIncorrectException {

        if (expense.getWallet() == null || expense.getCategory() == null) {
            User user = userService.findByUsername(principal.getName());
            List<Category> categories = categoryService.getExpenseCategories(user.getId());
            List<Wallet> wallets = walletService.getAllWallets(user.getId());
            if (expense.getWallet() == null) {
                model.addAttribute("walletError", "You must select a wallet first");
                model.addAttribute("expense", expense);
                model.addAttribute("wallets", wallets);
                model.addAttribute("categories", categories);
                return "add-expense";
            }
            if (expense.getCategory() == null) {
                model.addAttribute("categoryError", "You must select a category first");
                model.addAttribute("expense", expense);
                model.addAttribute("wallets", wallets);
                model.addAttribute("categories", categories);
                return "add-expense";
            }
        }

        Optional<Limit> limitOptional = expense.getWallet().getLimits().stream()
                .filter(l -> l.getCategory().equals(expense.getCategory()))
                .findAny();
        if (limitOptional.isPresent()) {
            if (limitOptional.get().getValue().subtract(expense.getAmount())
                    .compareTo(BigDecimal.valueOf(0)) >= 0) {
                transactionService.saveExpense(expense);
            } else if (warning != null) {
                transactionService.saveExpense(expense);
            } else {
                User user = userService.findByUsername(principal.getName());
                List<Category> categories = categoryService.getExpenseCategories(user.getId());
                List<Wallet> wallets = walletService.getAllWallets(user.getId());
                model.addAttribute("limitWarning", "Превышение лимита по категории");
                model.addAttribute("expense", expense);
                model.addAttribute("wallets", wallets);
                model.addAttribute("categories", categories);
                return "add-expense";
            }
        }

        transactionService.saveExpense(expense);
        return "redirect:/";
    }

    @GetMapping("/editIncome/{id}")
    public String showIncomeExpensePage(@PathVariable("id") UUID id, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Category> categories = categoryService.getIncomeCategories(user.getId());
        List<Wallet> wallets = walletService.getAllWallets(user.getId());
        Transaction income = transactionService.getTransactionById(id);
        model.addAttribute("income", income);
        model.addAttribute("wallets", wallets);
        model.addAttribute("categories", categories);
        return "update-income";
    }

    @PostMapping("/updateIncome/{id}")
    public String updateIncome(@PathVariable("id") UUID id, @ModelAttribute("income") @Valid Transaction income,
                               Model model, Principal principal)
            throws WalletNotFoundException, CategoryNotFoundException, TransactionIncorrectException {
        if (income.getWallet() == null || income.getCategory() == null) {
            User user = userService.findByUsername(principal.getName());
            List<Category> categories = categoryService.getExpenseCategories(user.getId());
            List<Wallet> wallets = walletService.getAllWallets(user.getId());
            if (income.getWallet() == null) {
                model.addAttribute("walletError", "You must select a wallet first");
                model.addAttribute("income", income);
                model.addAttribute("wallets", wallets);
                model.addAttribute("categories", categories);
                return "update-income";
            }
            if (income.getCategory() == null) {
                model.addAttribute("categoryError", "You must select a category first");
                model.addAttribute("income", income);
                model.addAttribute("wallets", wallets);
                model.addAttribute("categories", categories);
                return "update-income";
            }
        }
        transactionService.updateIncome(id, income);
        return "redirect:/";
    }

    @GetMapping("/editExpense/{id}")
    public String showUpdateExpensePage(@PathVariable("id") UUID id, Model model, Principal principal,
                                        @ModelAttribute("warning") String warning) {
        User user = userService.findByUsername(principal.getName());
        List<Category> categories = categoryService.getExpenseCategories(user.getId());
        List<Wallet> wallets = walletService.getAllWallets(user.getId());
        if (!model.containsAttribute("expense")) {
            Transaction expense = transactionService.getTransactionById(id);
            model.addAttribute("expense", expense);
        }
        model.addAttribute("wallets", wallets);
        model.addAttribute("categories", categories);
        return "update-expense";
    }

    @PostMapping("/updateExpense/{id}")
    public String updateExpense(@PathVariable("id") UUID id, @ModelAttribute("expense") @Valid Transaction expense,
                                @ModelAttribute("warning") String warning, Model model, Principal principal)
            throws WalletNotFoundException, CategoryNotFoundException, TransactionIncorrectException{
        if (expense.getWallet() == null || expense.getCategory() == null) {
            User user = userService.findByUsername(principal.getName());
            List<Category> categories = categoryService.getExpenseCategories(user.getId());
            List<Wallet> wallets = walletService.getAllWallets(user.getId());
            if (expense.getWallet() == null) {
                model.addAttribute("walletError", "You must select a wallet first");
                model.addAttribute("expense", expense);
                model.addAttribute("wallets", wallets);
                model.addAttribute("categories", categories);
                return "redirect:/editExpense/" + id;
            }
            if (expense.getCategory() == null) {
                model.addAttribute("categoryError", "You must select a category first");
                model.addAttribute("expense", expense);
                model.addAttribute("wallets", wallets);
                model.addAttribute("categories", categories);
                return "redirect:/editExpense/" + id;
            }
        }

        Optional<Limit> limitOptional = expense.getWallet().getLimits().stream()
                .filter(l -> l.getCategory().equals(expense.getCategory()))
                .findAny();
        if (limitOptional.isPresent()) {
            if (limitOptional.get().getValue().add(expense.getAmount())
                    .compareTo(BigDecimal.valueOf(0)) >= 0) {
                transactionService.updateExpense(id, expense);
            } else if (warning != null) {
                transactionService.updateExpense(id, expense);
            } else {
                model.addAttribute("limitWarning", "Превышение лимита по категории");
                return "redirect:/editExpense/" + id;
            }
        }
        transactionService.updateExpense(id, expense);
        return "redirect:/";
    }

    @DeleteMapping("/deleteTransaction/{id}")
    public String deleteExpense(@PathVariable("id") UUID id) {
        transactionService.deleteTransactionById(id);
        return "redirect:/";
    }
}
