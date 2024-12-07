package com.bnpp.kata.onlinebookstore.service;

import com.bnpp.kata.onlinebookstore.entity.Users;
import com.bnpp.kata.onlinebookstore.exception.UnauthorizedException;
import com.bnpp.kata.onlinebookstore.exception.UserNotFoundException;
import com.bnpp.kata.onlinebookstore.repository.UserRepository;
import com.bnpp.kata.onlinebookstore.models.UserLoginRequest;
import com.bnpp.kata.onlinebookstore.models.UserLoginResponse;
import static com.bnpp.kata.onlinebookstore.constants.Constants.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserLoginResponse registerUser (UserLoginRequest registerRequest) {

        if(checkUserExists (registerRequest.getUsername())){
            return createResponse(USER_EXISTS, false);
        }

        registerNewUser(registerRequest);
        return createResponse(REGISTER_SUCCESS, true);
    }

    private UserLoginResponse createResponse(String message, boolean isValid) {

        return UserLoginResponse.builder()
                .message(message)
                .validResponse (isValid)
                .build();
    }
    private boolean checkUserExists (String username) {
        return userRepository.existsByUsername (username);
    }

    private void registerNewUser (UserLoginRequest registerRequest) {

        Users user = Users.builder ()
                .username (registerRequest.getUsername ())
                .firstname (registerRequest.getFirstName ())
                .lastname (registerRequest.getLastName ())
                .password (passwordEncoder.encode (registerRequest.getPassword ()))
                .build ();

        userRepository.save (user);
    }

    public UserLoginResponse loginUser (UserLoginRequest userLoginRequest) {

        validateUserExists (userLoginRequest);
        validateUserCredentials (userLoginRequest);
        return createResponse (REGISTER_SUCCESS, true);
    }

    private void validateUserExists(UserLoginRequest userLoginRequest) {
        if (!checkUserExists(userLoginRequest.getUsername())) {
            throw new UserNotFoundException(USER_NOT_EXISTS);
        }
    }
    private void validateUserCredentials(UserLoginRequest userLoginRequest) {
        if (!validateLogin(userLoginRequest.getUsername(), userLoginRequest.getPassword())) {
            throw new UnauthorizedException(INVALID_CREDENTIALS);
        }
    }

    public boolean validateLogin(String username, String password) {
        Users user = userRepository.findByUsername (username);
        return ( passwordEncoder.matches (password, user.getPassword ()));
    }
}
