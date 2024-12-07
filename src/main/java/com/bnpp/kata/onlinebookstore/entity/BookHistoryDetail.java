package com.bnpp.kata.onlinebookstore.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@AllArgsConstructor
@Builder
public class BookHistoryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bookId;
    private int quantity;
}