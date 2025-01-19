package com.dyalex.personalfinancemephi.service;

import com.dyalex.personalfinancemephi.model.Limit;
import com.dyalex.personalfinancemephi.repository.LimitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class LimitService {
    private final LimitRepository limitRepository;

    public void saveLimit(Limit limit) {
        limitRepository.save(limit);
    }

    public Limit getLimitById(long id) {
        return limitRepository.findById(id).orElse(null);
    }

    public void updateLimit(long id, Limit limit) {
        Limit oldLimit = limitRepository.findById(id).orElse(null);
        if (oldLimit != null) {
            Limit newLimit = Limit.builder()
                    .id(oldLimit.getId())
                    .wallet(oldLimit.getWallet())
                    .category(limit.getCategory())
                    .value(limit.getValue())
                    .build();
            limitRepository.save(newLimit);
        }
    }

    public void deleteLimitById(long id) {
        limitRepository.deleteById(id);
    }

    public Optional<Limit> getLimitByWalletAndCategory(Long walletId, Long categoryId) {
        return limitRepository.findByWalletIdAndCategoryId(walletId, categoryId);
    }
}
