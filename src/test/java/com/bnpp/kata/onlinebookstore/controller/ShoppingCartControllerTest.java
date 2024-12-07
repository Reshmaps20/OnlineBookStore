package com.bnpp.kata.onlinebookstore.controller;

import com.bnpp.kata.onlinebookstore.entity.Users;
import com.bnpp.kata.onlinebookstore.repository.UserRepository;
import com.bnpp.kata.onlinebookstore.service.ShoppingCartService;
import com.bnpp.kata.onlinebookstore.models.CartRequest;
import com.bnpp.kata.onlinebookstore.models.CartResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.bnpp.kata.onlinebookstore.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Mock
    private ShoppingCartService shoppingCartService;

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll ();
    }

    @Test
    @DisplayName("Fetch Cart Details of the user")
    void getCartItems_fetchUserCart_shouldReturnListOfCartItems() throws Exception {

        List<CartResponse> mockCartItems = new ArrayList<> ();
        Users user = userRepository.save(Users.builder().username(USERNAME).password(PASSWORD).build());
        mockCartItems.add (CartResponse.builder ().build ());
        when(shoppingCartService.getCartItems(user.getId ())).thenReturn(mockCartItems);
        MockAuthentication (user);

        mockMvc.perform(get(CART_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private static void MockAuthentication (Users user) {

        Authentication mockAuthentication = mock (Authentication.class);
        when(mockAuthentication.getPrincipal()).thenReturn(user);

        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);

        SecurityContextHolder.setContext(mockSecurityContext);
    }

    @Test
    @DisplayName("Update the cart details")
    void updateCart_updateTheUserCart_shouldReturnListOfCartItems() throws Exception {

        List<CartResponse> mockCartItems = new ArrayList<> ();
        Users user = userRepository.save(Users.builder().username(USERNAME).password(PASSWORD).build());
        mockCartItems.add (CartResponse.builder ().build ());
        CartRequest cartRequest = CartRequest.builder().items (Collections.emptyList ()).ordered (false).build ();
        when(shoppingCartService.updateCart(user.getId (),cartRequest)).thenReturn(mockCartItems);
        MockAuthentication (user);

        mockMvc.perform(post(CART_UPDATE_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper ().writeValueAsString(cartRequest)))
                .andExpect(status().isOk ())
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("Update the cart details : Invalid User")
    void updateCart_invalidUser_shouldThrowException() throws Exception {

        List<CartResponse> mockCartItems = new ArrayList<> ();
        mockCartItems.add (CartResponse.builder ().build ());
        CartRequest cartRequest = CartRequest.builder().items (Collections.emptyList ()).ordered (false).build ();
        when(shoppingCartService.updateCart(null,cartRequest)).thenReturn(mockCartItems);
        MockAuthentication (null);

        mockMvc.perform(post(CART_UPDATE_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper ().writeValueAsString(cartRequest)))
                .andExpect(status().isBadRequest ());
    }

    @Test
    @DisplayName("Fetch Cart Details of an invalid user should throw exception")
    void getCartItems_userCartForInvalidUser_shouldThrowException() throws Exception {

        List<CartResponse> mockCartItems = new ArrayList<> ();
        mockCartItems.add (CartResponse.builder ().build ());
        when(shoppingCartService.getCartItems(null)).thenReturn(mockCartItems);
        MockAuthentication (null);

        mockMvc.perform(get(CART_API)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest ());
    }
}
