package com.bnpp.kata.onlinebookstore.service;

import com.bnpp.kata.onlinebookstore.entity.*;
import com.bnpp.kata.onlinebookstore.exception.UserNotFoundException;
import com.bnpp.kata.onlinebookstore.repository.*;
import com.bnpp.kata.onlinebookstore.models.BookRequest;
import com.bnpp.kata.onlinebookstore.models.CartRequest;
import com.bnpp.kata.onlinebookstore.models.CartResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static com.bnpp.kata.onlinebookstore.constants.TestConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ShoppingCartServiceTest {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ShoppingHistoryRepository shoppingHistoryRepository;

    @AfterEach
    void cleanUp() {
        shoppingHistoryRepository.deleteAll ();
        shoppingCartItemRepository.deleteAll ();
        shoppingCartRepository.deleteAll();
        bookRepository.deleteAll ();
        userRepository.deleteAll ();
    }
    @Test
    @DisplayName("Cart Details : No cart available , return empty list ")
    void getCartItems_noItemsAddedToCart_returnsEmptyList() {

        List<CartResponse> result = shoppingCartService.getCartItems (USERID);

        assertThat(result.isEmpty ());
    }

    @Test
    @DisplayName("Cart Details : Check user have entry in shopping cart")
    void getCartItems_checkUserHaveShoppingCartEntry_returnsResponse() {

        Users user = createUserRepo();
        ShoppingCart shoppingCartUser = createShoppingCartRepo(user);

        List<CartResponse> result = shoppingCartService.getCartItems (user.getId ());

        assertThat(!result.isEmpty ());
    }

    @Test
    @DisplayName("Cart Details : Fetch the cart details of the user")
    void getCartItems_fetchTheCartDetailsOfTheUser_returnsListOfCartItems() {

        Users user = createUserRepo();
        ShoppingCart shoppingCartUser = createShoppingCartRepo(user);
        Books books = createBooksRepo();
        ShoppingCartItem shoppingCartItem = createShoppingCartItemRepo(shoppingCartUser,books);

        List<CartResponse> result = shoppingCartService.getCartItems (user.getId ());

        assertThat(result.get (0).getBook ().getTitle ()).isEqualTo (BOOK_NAME);
    }

    private Users createUserRepo(){
        return userRepository.save(Users.builder().username(USERNAME).password(PASSWORD).build());
    }

    private ShoppingCart createShoppingCartRepo(Users user){
        return shoppingCartRepository.save(ShoppingCart.builder ().user (user).build());
    }

    private Books createBooksRepo(){
        return bookRepository.save (Books.builder ().title (BOOK_NAME).author (FIRSTNAME).price (PRICE).build ());
    }

    private ShoppingCartItem createShoppingCartItemRepo(ShoppingCart shoppingCartUser , Books books){
        return  shoppingCartItemRepository.save(ShoppingCartItem.builder ().shoppingCart (shoppingCartUser)
                .book (books).quantity (BOOK_COUNT).build ());
    }

    @Test
    @DisplayName("Update Cart Details : Return the cart if the user have one")
    void updateCart_userHaveACart_returnsUserCart() {

        Users user = createUserRepo();
        ShoppingCart shoppingCartUser = createShoppingCartRepo(user);
        List<BookRequest> bookrequestList = createBookRequest();
        CartRequest cartRequest = CartRequest.builder()
                .items (Collections.EMPTY_LIST).ordered (false).build ();

        List<CartResponse> result = shoppingCartService.updateCart (user.getId (),cartRequest);

        assertThat(result.size ()).isZero ();
    }

    private List<BookRequest> createBookRequest(){
        List<BookRequest> bookrequestList = new ArrayList<> ();
        BookRequest bookrequest = BookRequest.builder ().bookId (BOOKID).quantity (BOOK_COUNT).build ();
        bookrequestList.add (bookrequest) ;
        return bookrequestList;
    }

    @Test
    @DisplayName("Update Cart Details : Create cart if not available")
    void updateCart_createShoppingCartIfNotAvailable_returnsUserCart() {

        Users user = createUserRepo();
        List<BookRequest> bookrequestList = createBookRequest();
        CartRequest cartRequest = CartRequest.builder()
                .items (Collections.EMPTY_LIST).ordered (false).build ();

        List<CartResponse> result = shoppingCartService.updateCart (user.getId (),cartRequest);

        assertThat(result.size ()).isZero ();
    }
    @Test
    @DisplayName("Update Cart Details : If User is not available throw error")
    void updateCart_userNotAvailable_throwUserNotFoundException() {

        List<BookRequest> bookrequestList = createBookRequest();
        CartRequest cartRequest = CartRequest.builder()
                .items (bookrequestList).ordered (false).build ();

        assertThrows(UserNotFoundException.class, () -> shoppingCartService.updateCart (USERID,cartRequest));
    }

    @Test
    @DisplayName("Update Cart Details : Return empty list if the request is empty")
    void updateCart_checkIfTheRequestIsEmpty_returnsEmptyList() {

        Users user = createUserRepo();
        CartRequest cartRequest = CartRequest.builder()
                .items (Collections.EMPTY_LIST)
                .ordered (false).build ();

        List<CartResponse> result = shoppingCartService.updateCart (user.getId (),cartRequest);

        assertThat(result.isEmpty ());
    }

    @Test
    @DisplayName("Update Cart Details : User add one item to the cart")
    void updateCart_userAddOneItemToCart_returnsCartDetails() {

        Users user = createUserRepo();
        List<BookRequest> bookrequestList = new ArrayList<> ();
        BookRequest bookrequest = BookRequest.builder ().bookId (createBooksRepo().getId ()).quantity (BOOK_COUNT).build ();
        bookrequestList.add (bookrequest) ;
        CartRequest cartRequest = CartRequest.builder()
                .items (bookrequestList)
                .ordered (false).build ();

        List<CartResponse> result = shoppingCartService.updateCart (user.getId (),cartRequest);

        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId (user.getId ());
        List<ShoppingCartItem> shoppingCartItem = shoppingCartItemRepository.findByShoppingCartId (shoppingCart.get ().getId ());
        assertThat(shoppingCartItem.size ()).isEqualTo (ONE);
    }

    @Test
    @DisplayName("Update Cart Details : User add multiple items to the cart")
    void updateCart_userAddMultipleItemsToCart_returnsCartDetails() {

        Users user = createUserRepo();
        Books bookOne = createBooksRepo();
        createShoppingCartItemRepo(createShoppingCartRepo(createUserRepo()),bookOne);
        Books bookTwo=bookRepository.save (Books.builder ().title (SECOND_BOOK_NAME).author (FIRSTNAME).price (PRICE).build ());
        List<BookRequest> bookrequestList = new ArrayList<> ();
        bookrequestList.add (BookRequest.builder ().bookId (bookOne.getId ()).quantity (BOOK_COUNT).build ()) ;
        bookrequestList.add (BookRequest.builder ().bookId (bookTwo.getId ()).quantity (ONE).build ()) ;
        CartRequest cartRequest = CartRequest.builder().items (bookrequestList).ordered (false).build ();

        List<CartResponse> result = shoppingCartService.updateCart (user.getId (),cartRequest);

        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId (user.getId ());
        List<ShoppingCartItem> shoppingCartItem = shoppingCartItemRepository.findByShoppingCartId (shoppingCart.get ().getId ());
        assertThat(shoppingCartItem.size ()).isEqualTo (2);
    }

    @Test
    @DisplayName("Update Cart Details : User updates the existing item quantity")
    void updateCart_userUpdatesItemQuantity_returnsNewQuantity() {

        Users user = createUserRepo();
        Books bookOne = createBooksRepo();
        ShoppingCartItem initialShoppingCartItem = createShoppingCartItemRepo(createShoppingCartRepo(createUserRepo()),bookOne);
        List<BookRequest> bookrequestList = new ArrayList<> ();
        bookrequestList.add (BookRequest.builder ().bookId (bookOne.getId ()).quantity (ONE).build ()) ;
        CartRequest cartRequest = CartRequest.builder().items (bookrequestList).ordered (false).build ();

        List<CartResponse> result = shoppingCartService.updateCart (user.getId (),cartRequest);

        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId (user.getId ());
        List<ShoppingCartItem> newShoppingCartItem = shoppingCartItemRepository.findByShoppingCartId (shoppingCart.get ().getId ());
        assertThat(initialShoppingCartItem.getQuantity ()).isNotEqualTo (newShoppingCartItem.get (0).getQuantity ());
    }

    @Test
    @DisplayName("Update Cart Details : Once user updates return CartResponse")
    void updateCart_userUpdatesItemSuccessfully_returnsCartResponse() {

        CartRequest cartRequest = CartRequest.builder().items (createBookRequest()).ordered (false).build ();

        List<CartResponse> result = shoppingCartService.updateCart (createUserRepo().getId (),cartRequest);

        assertThat(result.get (0).getBook ().getTitle ()).isEqualTo (BOOK_NAME_FOR_ID_10);
    }

    @Test
    @DisplayName("Update Cart Details : Delete the cart details if the cart request is empty")
    void updateCart_deleteCartDetailsForEmptyRequest_returnsEmptyCart() {

        Users user = createUserRepo();
        CartRequest cartRequest = CartRequest.builder().items (Collections.EMPTY_LIST).ordered (false).build ();

        List<CartResponse> result = shoppingCartService.updateCart (user.getId (),cartRequest);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId (user.getId ()).orElse (null);
        assertThat(shoppingCart).isNull ();
    }

    @Test
    @DisplayName("Update Cart Details : If the requested book is not available throw error")
    void updateCart_requestedBookNotAvailable_throwException() {

        List<BookRequest> bookrequestList = createBookRequest();
        CartRequest cartRequest = CartRequest.builder()
                .items (bookrequestList).ordered (false).build ();

        assertThrows(Exception.class, () -> shoppingCartService.updateCart (USERID,cartRequest));
    }

    @Test
    @DisplayName("Update Cart Details : Once checkout completes add the details to the history table")
    void updateCart_checkoutDetailsAddToHistoryTable_returnsCartResponse() {

        Users user = createUserRepo();
        Books bookOne = createBooksRepo();
        List<BookRequest> bookrequestList = new ArrayList<> ();
        BookRequest bookrequest = BookRequest.builder ().bookId (bookOne.getId ()).quantity (BOOK_COUNT).build ();
        bookrequestList.add (bookrequest) ;
        CartRequest cartRequest = CartRequest.builder().items (bookrequestList).ordered (true).build ();

        List<CartResponse> result = shoppingCartService.updateCart (user.getId (),cartRequest);

        Optional<ShoppingHistory> shoppingHistory = shoppingHistoryRepository.findById (1L);
        assertThat(shoppingHistory.get().getUser ().getId ()).isEqualTo (user.getId ());
    }

    @Test
    @DisplayName("Update Cart Details : Once checkout details saved in history remove the shopping cart")
    void updateCart_removeShoppingCartDetailsOnceHistoryIsSaved_returnsCartResponse() {

        Users user = createUserRepo();
        Books bookOne = createBooksRepo();
        List<BookRequest> bookrequestList = new ArrayList<> ();
        BookRequest bookrequest = BookRequest.builder ().bookId (bookOne.getId ()).quantity (BOOK_COUNT).build ();
        bookrequestList.add (bookrequest) ;
        CartRequest cartRequest = CartRequest.builder().items (bookrequestList).ordered (true).build ();

        List<CartResponse> result = shoppingCartService.updateCart (user.getId (),cartRequest);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId (user.getId ()).orElse (null);
        assertThat(shoppingCart).isNull ();
    }

    @Test
    @DisplayName("Update Cart Details : Once checkout calculate the total price and save it in history table")
    void updateCart_calculateTotalPriceOfItemsPurchased_returnsCartResponse() {

        Users user = createUserRepo();
        Books bookOne = createBooksRepo();
        List<BookRequest> bookrequestList = new ArrayList<> ();
        BookRequest bookrequest = BookRequest.builder ().bookId (bookOne.getId ()).quantity (BOOK_COUNT).build ();
        bookrequestList.add (bookrequest) ;
        CartRequest cartRequest = CartRequest.builder().items (bookrequestList).ordered (true).build ();

        List<CartResponse> result = shoppingCartService.updateCart (user.getId (),cartRequest);

        List<ShoppingHistory> shoppingHistory = shoppingHistoryRepository.findAll ();
        assertThat(shoppingHistory.get (0).getTotalPrice ()).isEqualTo (1000.0);
    }
}
