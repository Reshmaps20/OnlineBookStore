package com.bnpp.kata.onlinebookstore.repository;

import com.bnpp.kata.onlinebookstore.entity.ShoppingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingHistoryRepository extends JpaRepository<ShoppingHistory, Long> {
}
