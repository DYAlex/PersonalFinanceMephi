package com.dyalex.personalfinancemephi.controller;

import com.dyalex.personalfinancemephi.model.Transaction;
import com.dyalex.personalfinancemephi.model.User;
import com.dyalex.personalfinancemephi.model.Wallet;
import com.dyalex.personalfinancemephi.service.FinanceService;
import com.dyalex.personalfinancemephi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Controller
public class FinanceController {
    private final FinanceService financeService;
    private final UserService userService;

    @GetMapping("/")
    public String viewHomePage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        List<Wallet> wallets = financeService.getAllWallets(user.getId());
        List<Transaction> expenses = financeService.getAllTransactions(user.getId());
        BigDecimal totalAmount = expenses.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("wallets", wallets);
        model.addAttribute("expenses", expenses);
        model.addAttribute("totalAmount", totalAmount);
        return "index";
    }

    @GetMapping("/addWallet")
    public String createWallet(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setLimits(Collections.emptyList());
        wallet.setTransactions(Collections.emptyList());
        model.addAttribute("wallet", wallet);
        return "add-wallet";
    }

    @PostMapping("/saveWallet")
    public String saveWallet(@ModelAttribute("wallet") Wallet wallet){
        financeService.saveWallet(wallet);
        return "redirect:/";
    }

    @GetMapping("/editWallet/{id}")
    public String showUpdateWalletPage(@PathVariable("id") long id, Model model) {
        Wallet wallet = financeService.getWalletById(id);
        model.addAttribute("wallet", wallet);
        return "update-wallet";
    }

    @PostMapping("/updateWallet/{id}")
    public String updateWallet(@PathVariable("id") long id, @ModelAttribute("wallet") Wallet wallet) {
        financeService.updateWallet(id, wallet);
        return "redirect:/";
    }

    //TODO: add category
    //TODO: add limit

    @GetMapping("/addExpense")
    public String showAddExpensePage(Model model) {
        Transaction expense = new Transaction();
        model.addAttribute("expense", expense);
        return "add-expense";
    }

    @PostMapping("/saveExpense")
    public String saveExpense(@ModelAttribute("expense") Transaction expense) {
        financeService.saveExpense(expense);
        return "redirect:/";
    }

    @GetMapping("/editExpense/{id}")
    public String showUpdateExpensePage(@PathVariable("id") long id, Model model) {
        Transaction expense = financeService.getExpenseById(id);
        model.addAttribute("expense", expense);
        return "update-expense";
    }

    @PostMapping("/updateExpense/{id}")
    public String updateExpense(@PathVariable("id") long id, @ModelAttribute("expense") Transaction expense, Model model) {
        financeService.updateExpense(id, expense);
        return "redirect:/";
    }

    @GetMapping("/deleteExpense/{id}")
    public String deleteExpense(@PathVariable("id") long id) {
        financeService.deleteExpenseById(id);
        return "redirect:/";
    }
}
