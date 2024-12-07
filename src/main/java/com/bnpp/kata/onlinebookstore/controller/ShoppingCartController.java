package com.bnpp.kata.onlinebookstore.controller;

import com.bnpp.kata.onlinebookstore.entity.Users;
import com.bnpp.kata.onlinebookstore.exception.UserNotFoundException;
import com.bnpp.kata.onlinebookstore.service.ShoppingCartService;
import com.bnpp.kata.onlinebookstore.models.CartRequest;
import com.bnpp.kata.onlinebookstore.models.CartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import static com.bnpp.kata.onlinebookstore.constants.Constants.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("${onlinebookstore.endpoint.cart}")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    @GetMapping
    public ResponseEntity<List<CartResponse>> getCartItems () {

        Users currentUser =  getCurrentUser();
        if (Objects.nonNull(currentUser) ) {
            return ResponseEntity.ok (shoppingCartService.getCartItems (currentUser.getId ()));
        }
        throw new UserNotFoundException (USER_NOT_EXISTS);
    }

    public Users getCurrentUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Users) authentication.getPrincipal();
    }

    @PostMapping("${onlinebookstore.endpoint.updateCart}")
    public ResponseEntity<List<CartResponse>> updateCart(@RequestBody CartRequest cartRequests) {

        Users currentUser = getCurrentUser();
        if (Objects.nonNull(currentUser) ) {
            return ResponseEntity.ok (shoppingCartService.updateCart (currentUser.getId (), cartRequests));
        }
        throw new UserNotFoundException (USER_NOT_EXISTS);
    }
}