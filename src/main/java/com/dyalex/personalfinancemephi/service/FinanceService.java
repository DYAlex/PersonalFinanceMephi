package com.dyalex.personalfinancemephi.service;

import com.dyalex.personalfinancemephi.model.Transaction;
import com.dyalex.personalfinancemephi.model.Wallet;
import com.dyalex.personalfinancemephi.repository.CategoryRepository;
import com.dyalex.personalfinancemephi.repository.LimitRepository;
import com.dyalex.personalfinancemephi.repository.TransactionRepository;
import com.dyalex.personalfinancemephi.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FinanceService {
    private final CategoryRepository categoryRepository;
    private final LimitRepository limitRepository;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;


    public List<Transaction> getAllTransactions(Long userId) {
        List<Wallet> wallets = walletRepository.findAllByUserId(userId);
        if (!wallets.isEmpty()) {
            return wallets.stream()
                    .flatMap(wallet -> wallet.getTransactions().stream())
                    .collect(Collectors.toList());
        }
        return transactionRepository.findAll();
    }

    public List<Wallet> getAllWallets(Long userId) {
        return walletRepository.findAllByUserId(userId);
    }

    public void saveExpense(Transaction expense) {
        // TODO: validate expense
        if (expense.getDate() == null) {
            expense.setDate(LocalDateTime.now());
        }
        if (expense.getCategory() == null) {
//            expense.setCategory("DEFAULT");
        }
        transactionRepository.save(expense);
    }

    public Transaction getExpenseById(long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public void updateExpense(long id, Transaction expense) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction != null) {
            Transaction newTransaction = Transaction.builder()
                    .id(transaction.getId())
                    .amount(expense.getAmount() != null ? expense.getAmount() : transaction.getAmount())
                    .date(expense.getDate() != null ? expense.getDate() : LocalDateTime.now())
                    .category(expense.getCategory() != null ? expense.getCategory() : transaction.getCategory())
                    .wallet(expense.getWallet() != null ? expense.getWallet() : transaction.getWallet())
                    .build();
            transactionRepository.save(newTransaction);
        }
    }

    public void deleteExpenseById(long id) {
        transactionRepository.deleteById(id);
    }

    public void saveWallet(Wallet wallet) {
        // TODO: validation
        walletRepository.save(wallet);
    }

    public Wallet getWalletById(long id) {
        return walletRepository.findById(id).orElse(null);
    }

    public void updateWallet(long id, Wallet wallet) {
        Wallet oldWallet = walletRepository.findById(id).orElse(null);
        if (oldWallet != null) {
            Wallet newWallet = Wallet.builder()
                    .id(oldWallet.getId())
                    .user(oldWallet.getUser())
                    .name(wallet.getName() != null ? wallet.getName() : oldWallet.getName())
                    .balance(wallet.getBalance() != null ? wallet.getBalance() : oldWallet.getBalance())
                    .build();
            walletRepository.save(newWallet);
        }
    }
}
