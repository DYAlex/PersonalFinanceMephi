package com.dyalex.personalfinancemephi.service;

import com.dyalex.personalfinancemephi.model.Transaction;
import com.dyalex.personalfinancemephi.model.TransactionType;
import com.dyalex.personalfinancemephi.model.Wallet;
import com.dyalex.personalfinancemephi.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public List<Wallet> getAllWallets(Long userId) {
        return setInBound(walletRepository.findAllByUserId(userId));
    }

    public void saveWallet(Wallet wallet) {
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

    public void deleteWalletById(long id) {
        walletRepository.deleteById(id);
    }

    public List<Wallet> getWalletsByIds(Long userId, List<Long> walletIds) {
        return setInBound(walletRepository.findAllByUserIdAndIdIsIn(userId, walletIds));
    }

    private List<Wallet> setInBound(List<Wallet> wallets) {
        return wallets.stream()
                .peek(wallet -> wallet.setLimits(wallet.getLimits().stream()
                        .peek(lim -> lim.setBalance(lim.getValue().subtract(lim.getWallet().getTransactions()
                                .stream()
                                .filter(transaction -> transaction.getCategory() == lim.getCategory())
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add))))
                                .peek(limit -> System.out.println(limit.getBalance()))
                        .peek(limit -> limit.setInBound(limit.getValue().compareTo(limit.getBalance().abs()) >= 0))
                                .peek(limit -> System.out.println(limit.getInBound()))
                        .toList()))
                .peek(wallet -> wallet.setInBound(wallet.getTransactions().stream()
                        .filter(transaction -> transaction.getCategory().getType() == TransactionType.INCOME)
                        .map(Transaction::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .compareTo(wallet.getTransactions().stream()
                                .filter(transaction -> transaction.getCategory().getType() == TransactionType.EXPENSE)
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add).abs()) >= 0)
                )
                .collect(Collectors.toList());
    }
}
