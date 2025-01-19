package com.dyalex.personalfinancemephi.repository;

import com.dyalex.personalfinancemephi.model.Category;
import com.dyalex.personalfinancemephi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findById(UUID id);

    void deleteById(UUID id);

    List<Transaction> findAllByCategoryIsIn(Collection<Category> category);
}
