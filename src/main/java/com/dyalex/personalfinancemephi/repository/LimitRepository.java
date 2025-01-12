package com.dyalex.personalfinancemephi.repository;

import com.dyalex.personalfinancemephi.model.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {

}
