package com.bnpp.kata.onlinebookstore.repository;

import com.bnpp.kata.onlinebookstore.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository  extends JpaRepository<Books, Long> {
}