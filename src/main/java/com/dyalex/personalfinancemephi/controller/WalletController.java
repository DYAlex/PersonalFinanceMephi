package com.dyalex.personalfinancemephi.controller;

import com.dyalex.personalfinancemephi.model.User;
import com.dyalex.personalfinancemephi.model.Wallet;
import com.dyalex.personalfinancemephi.service.UserService;
import com.dyalex.personalfinancemephi.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;

@AllArgsConstructor
@Controller
public class WalletController {
    private final WalletService walletService;
    private final UserService userService;

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
        walletService.saveWallet(wallet);
        return "redirect:/";
    }

    @GetMapping("/editWallet/{id}")
    public String showUpdateWalletPage(@PathVariable("id") long id, Model model) {
        Wallet wallet = walletService.getWalletById(id);
        model.addAttribute("wallet", wallet);
        return "update-wallet";
    }

    @PostMapping("/updateWallet/{id}")
    public String updateWallet(@PathVariable("id") long id, @ModelAttribute("wallet") Wallet wallet) {
        walletService.updateWallet(id, wallet);
        return "redirect:/";
    }

    @DeleteMapping("/deleteWallet/{id}")
    public String deleteExpense(@PathVariable("id") long id) {
        walletService.deleteWalletById(id);
        return "redirect:/";
    }
}
