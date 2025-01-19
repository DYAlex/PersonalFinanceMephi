package com.dyalex.personalfinancemephi.repository;

import com.dyalex.personalfinancemephi.model.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {

    Optional<Limit> findByWalletIdAndCategoryId(Long walletId, Long categoryId);
}
