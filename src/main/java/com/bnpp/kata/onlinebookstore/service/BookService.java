package com.bnpp.kata.onlinebookstore.service;

import com.bnpp.kata.onlinebookstore.entity.Books;
import com.bnpp.kata.onlinebookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    public List<Books> getAllBooks() {
        return bookRepository.findAll();
    }

}
