package com.dyalex.personalfinancemephi.service;

import com.dyalex.personalfinancemephi.model.Category;
import com.dyalex.personalfinancemephi.model.Transaction;
import com.dyalex.personalfinancemephi.model.TransactionType;
import com.dyalex.personalfinancemephi.model.Wallet;
import com.dyalex.personalfinancemephi.repository.TransactionRepository;
import com.dyalex.personalfinancemephi.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final WalletRepository walletRepository;

    public List<Transaction> getAllTransactions(Long userId) {
        List<Wallet> wallets = walletService.getAllWallets(userId);
        if (!wallets.isEmpty()) {
            return wallets.stream()
                    .flatMap(wallet -> wallet.getTransactions().stream())
                    .collect(Collectors.toList());
        }
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(UUID id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteTransactionById(UUID id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction != null) {
            TransactionType type = transaction.getCategory().getType();
            Wallet wallet = transaction.getWallet();
            switch (type) {
                case EXPENSE:
                    wallet.setBalance(wallet.getBalance().add(transaction.getAmount()));
                    walletRepository.save(wallet);
                    transactionRepository.deleteById(id);
                    return;
                case INCOME:
                    wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount()));
                    walletRepository.save(wallet);
                    transactionRepository.deleteById(id);
                    return;
                default:
                    transactionRepository.deleteById(id);
            }
        }
    }

    public void saveExpense(Transaction expense) {
        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }
        if (expense.getCategory() == null) {
            Category category = Category.builder()
                    .name("DEFAULT EXPENSE")
                    .type(TransactionType.EXPENSE)
                    .user(expense.getWallet().getUser())
                    .build();
            expense.setCategory(category);
        }
        Wallet wallet = walletService.getWalletById(expense.getWallet().getId());
        if (wallet != null) {
            wallet.setBalance(wallet.getBalance().subtract(expense.getAmount()));
            transactionRepository.save(expense);
            walletService.saveWallet(wallet);
        }
    }

    public void updateExpense(UUID id, Transaction expense) {
        Transaction oldTransaction = transactionRepository.findById(id).orElse(null);
        if (oldTransaction != null) {
            Transaction newTransaction = buildNewTransaction(oldTransaction, expense);

            Wallet wallet = walletService.getWalletById(expense.getWallet().getId());
            if (wallet != null) {
                wallet.setBalance(wallet.getBalance().subtract(expense.getAmount()));
                transactionRepository.save(newTransaction);
                walletService.saveWallet(wallet);
            }
        }
    }

    public void saveIncome(Transaction income) {
        if (income.getDate() == null) {
            income.setDate(LocalDate.now());
        }
        if (income.getCategory() == null) {
            Category category = Category.builder()
                    .name("DEFAULT INCOME")
                    .type(TransactionType.INCOME)
                    .user(income.getWallet().getUser())
                    .build();
            income.setCategory(category);
        }

        Wallet wallet = walletService.getWalletById(income.getWallet().getId());
        if (wallet != null) {
            wallet.setBalance(wallet.getBalance().add(income.getAmount()));
            transactionRepository.save(income);
            walletService.saveWallet(wallet);
        }
    }

    public void updateIncome(UUID id, Transaction income) {
        Transaction oldTransaction = transactionRepository.findById(id).orElse(null);
        if (oldTransaction != null) {
            Transaction newTransaction = buildNewTransaction(oldTransaction, income);
            Wallet wallet = walletService.getWalletById(income.getWallet().getId());
            if (wallet != null) {
                wallet.setBalance(wallet.getBalance().add(income.getAmount()));
                transactionRepository.save(newTransaction);
                walletService.saveWallet(wallet);
            }
        }
    }

    private Transaction buildNewTransaction(Transaction oldTransaction, Transaction transaction) {
        return Transaction.builder()
                .id(oldTransaction.getId())
                .amount(transaction.getAmount() != null ? transaction.getAmount() : oldTransaction.getAmount())
                .date(transaction.getDate() != null ? transaction.getDate() : LocalDate.now())
                .category(transaction.getCategory() != null ? transaction.getCategory() : oldTransaction.getCategory())
                .wallet(transaction.getWallet() != null ? transaction.getWallet() : oldTransaction.getWallet())
                .build();
    }
}
