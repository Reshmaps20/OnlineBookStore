package com.bnpp.kata.onlinebookstore.service;

import com.bnpp.kata.onlinebookstore.entity.Books;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static com.bnpp.kata.onlinebookstore.constants.TestConstants.*;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("Fetch All the available books")
    void getAllBooks_fetchBooksFromDB_returnsListOfBooks() {

        List<Books> result = bookService.getAllBooks ();

        assertThat (result.get(0).getTitle ()).isEqualTo (BOOK_NAME);
    }
}
