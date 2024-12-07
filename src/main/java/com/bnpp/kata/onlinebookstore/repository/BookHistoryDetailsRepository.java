package com.bnpp.kata.onlinebookstore.repository;

import com.bnpp.kata.onlinebookstore.entity.BookHistoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookHistoryDetailsRepository extends JpaRepository<BookHistoryDetail, Long> {
}
