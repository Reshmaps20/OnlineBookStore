import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import Login from "./login";


test('Displays Login header in the Login page', () => {

    render(<Login />);
    const titleElement = screen.getByTestId('login-heading');
    expect(titleElement).toBeInTheDocument();
})

test('Displays Username and password fields', () => {

    render(<Login />);
    const inputUsername = screen.getByPlaceholderText("Username");
    fireEvent.change(inputUsername, { target: { value: "User" } });
    expect(inputUsername.value).toBe("User");

    const inputPassword = screen.getByPlaceholderText("Password");
    fireEvent.change(inputPassword, { target: { value: "User123" } });
    expect(inputPassword.value).toBe("User123");
})

 test("Display Login and New User button on initial load", () => {

    render(<Login />);
    const loginButton = screen.getByTestId('login-id');
    expect(loginButton).toBeInTheDocument();

    const newUserButton = screen.getByTestId('register-id');
    expect(newUserButton).toBeInTheDocument();
  });

  test("Display more fields when user clicks on New User button", () => {

      render(<Login />);
        const registerButton = screen.getByTestId('register-id');
        fireEvent.click(registerButton);
        expect(screen.getByPlaceholderText('First Name')).toBeInTheDocument();
        expect(screen.getByPlaceholderText('Last Name')).toBeInTheDocument();
        expect(screen.getByPlaceholderText('Confirm Password')).toBeInTheDocument();

        const loginButton = screen.getByTestId('login-id');
        expect(loginButton).toBeInTheDocument();

        const newUserButton = screen.getByTestId('register-id');
        expect(newUserButton).toBeInTheDocument();
  });


test("Register button should be enabled only after filling all the fields", () => {

     render(<Login />);
        const newUserButton = screen.getByRole('button', { name: /New User/i });
        fireEvent.click(newUserButton);
        const registerButton = screen.getByRole('button', { name: /Register/i });
        expect(registerButton).toBeDisabled();

        const userNameInput = screen.getByPlaceholderText('Username');
        const firstNameInput = screen.getByPlaceholderText('First Name');
        const lastNameInput = screen.getByPlaceholderText('Last Name');
        const passwordInput = screen.getByPlaceholderText('Password');
        const confirmPasswordInput = screen.getByPlaceholderText('Confirm Password');

        fireEvent.change(userNameInput, { target: { value: 'testUser' } });
        fireEvent.change(firstNameInput, { target: { value: 'John' } });
        fireEvent.change(lastNameInput, { target: { value: 'Doe' } });
        fireEvent.change(passwordInput, { target: { value: 'password123' } });
        fireEvent.change(confirmPasswordInput, { target: { value: 'password123' } });

        expect(registerButton).toBeEnabled();
  });