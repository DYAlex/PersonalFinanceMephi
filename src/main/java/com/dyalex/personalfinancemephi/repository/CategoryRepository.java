package com.dyalex.personalfinancemephi.repository;

import com.dyalex.personalfinancemephi.model.Category;
import com.dyalex.personalfinancemephi.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserId(Long id);

    List<Category> findAllByUserIdAndType(Long id, TransactionType transactionType);

    List<Category> findAllByUserIdAndIdIsIn(Long userId, Collection<Long> ids);
}
