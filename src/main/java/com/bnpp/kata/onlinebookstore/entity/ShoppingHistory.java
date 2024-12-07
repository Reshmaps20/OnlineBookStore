package com.bnpp.kata.onlinebookstore.entity;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @OneToMany
    @JoinColumn(name = "history_id")
    private List<BookHistoryDetail> bookDetails;
    private double totalPrice;
}
