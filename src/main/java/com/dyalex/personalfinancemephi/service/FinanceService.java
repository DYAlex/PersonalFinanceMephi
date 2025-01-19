package com.dyalex.personalfinancemephi.service;

import com.dyalex.personalfinancemephi.model.Category;
import com.dyalex.personalfinancemephi.model.Transaction;
import com.dyalex.personalfinancemephi.model.TransactionType;
import com.dyalex.personalfinancemephi.model.Wallet;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FinanceService {
    private final CategoryService categoryService;
    private final TransactionService transactionService;
    private final WalletService walletService;

    public List<Transaction> getAllTransactions(Long userId) {
        return transactionService.getAllTransactions(userId);
    }

    public List<Wallet> getAllWallets(Long userId) {
        return walletService.getAllWallets(userId);
    }

    public List<Category> getAllCategories(Long id) {
        return categoryService.getAllCategories(id);
    }

    public List<Wallet> getWalletsByIds(List<Long> walletIds, Long userId) {
        return walletService.getWalletsByIds(userId, walletIds);
    }

    public List<Category> getCategoriesByIds(List<Long> categoryIds, Long userId) {
        return categoryService.getCategoriesByIds(userId, categoryIds);
    }

    public List<Transaction> getTransactionsByType(Long userId, TransactionType transactionType) {
        return transactionService.getTransactionsByType(userId, transactionType);
    }
}
