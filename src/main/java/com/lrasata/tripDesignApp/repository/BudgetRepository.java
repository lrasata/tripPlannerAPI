package com.lrasata.tripDesignApp.repository;

import com.lrasata.tripDesignApp.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {}
