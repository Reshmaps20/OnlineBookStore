package com.bnpp.kata.onlinebookstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    @JsonIgnore
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Books book;

    private int quantity;
}