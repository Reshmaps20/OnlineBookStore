import React, { useState, useEffect } from "react";
import axios from "axios";
import {useLocation } from "react-router-dom";
import "./index.css";

const BookStore = () => {
  const location = useLocation();
  const { username, password } = location.state || {};
  const [books, setBooks] = useState([]);
  const [error, setError] = useState(null);
   const [quantities, setQuantities] = useState({});

console.log("books:",books);
  const credentials = `${username}:${password}`;
  const encodedCredentials = btoa(credentials);

  useEffect(() => {
    fetchBooks();
  }, []);

const fetchBooks = () => {
    setError(null);
    console.log("credentials:",credentials);
    axios
      .get("http://localhost:8080/api/books", {
        headers: {
          Authorization: `Basic ${encodedCredentials}`,
        },
      })
      .then((response) => {
        setBooks(response.data);
        console.log("Books retrieved:", response.data);
      })
      .catch((error) => {
        setError("Error fetching books. Please try again.");
      });
  };

  const handleQuantityChange = (bookId, value) => {
    setQuantities((prev) => ({ ...prev, [bookId]: parseInt(value, 10) }));
  };

  const handleAddToCart = (book) => {
      const cartItemsToAdd = { bookId: book.id, quantity: quantities[book.id] || 1 };

      const updatedCart = [...cartItem, cartItemsToAdd]
      setCart(updatedCart);

      const addedBooksSet = new Set(updatedCart.map(item => item.bookId));
      setAddedBooks(addedBooksSet);
      console.log("addedBooksSet:", addedBooksSet);
      console.log("addedBooks:", addedBooks);

      const requestBody = {
        items: updatedCart,
        ordered: order,
      };
      console.log("requestBody:", requestBody);
      axios
        .post("http://localhost:8080/api/cart/updateCart", requestBody, {
          headers: {
            Authorization: `Basic ${encodedCredentials}`,
          },
        })
        .then((response) => {
          console.log(response.data);
          setAddedBooks((prev) => new Set(prev).add(book.id));
        })
        .catch((error) => {
          console.error("Error adding to cart:", error);
          alert("Failed to add book to cart.");
        });
    };

  return (
    <div className="bookscreen-container">
      <div className="header">
        <h1 className="bookstore-heading" data-testid="bookstore-heading">Online Book Store</h1>
        <button className="logout-btn" data-testid="logout-button">Logout</button>
      </div>
      <div className="user-greeting-container">
        <span className="username" data-testid="user-greeting">Hi,{username}</span>
              <button className="cart-btn" data-testid="cart-button"> Cart  </button>
      </div>
      <div className="book-list">
              {books.map((book) => (
                <div key={book.id} className="book-item" data-testid="book-item">
                  <h3 data-testid="book-title">{book.title}</h3>
                  <p>Author: {book.author}</p>
                  <p>Price: €{book.price}</p>

                <div className="book-actions">
                              <select
                                className="quantity-picker" data-testid={`quantity-picker-${book.id}`}
                                value={quantities[book.id] || 1}
                                onChange={(e) => handleQuantityChange(book.id, e.target.value)}
                              >
                                <option value="" disabled>
                                </option>
                                {[...Array(50).keys()].map((i) => (
                                  <option key={i + 1} value={i + 1}>
                                    {i + 1}
                                  </option>
                                ))}
                              </select>

                              <button
                                              onClick={() => handleAddToCart(book)}
                                              className="add-to-cart-btn"

                                            >
                                              Add to Cart
                                            </button>
 </div>
                </div>
              ))}
            </div>
    </div>
  );
};

export default BookStore;
