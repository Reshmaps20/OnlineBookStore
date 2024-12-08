import React from "react";
import { BrowserRouter as Router } from "react-router-dom";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import "@testing-library/jest-dom";
import BookStore from "./index";


test('Displays Online BookStore Heading', () => {

        render(
          <Router initialEntries={["/books", { state: { username: "username", password: "abc" } }]}>
            <BookStore />
          </Router>
        );
    const bookStoreTitle = screen.getByTestId('bookstore-heading');
    expect(bookStoreTitle).toBeInTheDocument();
    const userGreeting = screen.getByTestId('user-greeting');
    expect(userGreeting).toBeInTheDocument();
    const cartButton = screen.getByTestId('cart-button');
    expect(cartButton).toBeInTheDocument();
    const logoutButton = screen.getByTestId('logout-button');
    expect(logoutButton).toBeInTheDocument();
})