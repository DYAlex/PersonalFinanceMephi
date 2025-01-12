package com.dyalex.personalfinancemephi.repository;

import com.dyalex.personalfinancemephi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
