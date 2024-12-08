import React, { useState, useEffect } from "react";
import axios from "axios";
import {useLocation } from "react-router-dom";
import "./index.css";

const BookStore = () => {
  const location = useLocation();
  const { username, password } = location.state || {};

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
    </div>
  );
};

export default BookStore;
