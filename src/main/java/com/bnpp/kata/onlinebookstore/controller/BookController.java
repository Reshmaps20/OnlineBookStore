package com.bnpp.kata.onlinebookstore.controller;

import com.bnpp.kata.onlinebookstore.entity.Books;
import com.bnpp.kata.onlinebookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${onlinebookstore.endpoint.books}")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<Books> getAllBooks () {
        return bookService.getAllBooks ();
    }
}