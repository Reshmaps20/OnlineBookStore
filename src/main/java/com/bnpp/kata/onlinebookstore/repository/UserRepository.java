package com.bnpp.kata.onlinebookstore.repository;

import com.bnpp.kata.onlinebookstore.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);
    boolean existsByUsername(String username);
}
