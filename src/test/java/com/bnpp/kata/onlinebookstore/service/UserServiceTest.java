package com.bnpp.kata.onlinebookstore.service;

import com.bnpp.kata.onlinebookstore.entity.Users;
import com.bnpp.kata.onlinebookstore.exception.UnauthorizedException;
import com.bnpp.kata.onlinebookstore.exception.UserNotFoundException;
import com.bnpp.kata.onlinebookstore.repository.UserRepository;
import com.bnpp.kata.onlinebookstore.models.UserLoginRequest;
import com.bnpp.kata.onlinebookstore.models.UserLoginResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static com.bnpp.kata.onlinebookstore.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName ("Register New User : User Registration Successful")
    void registerUser_newUserRegistration_returnsSuccessMessage() {

        UserLoginRequest userLoginRequest = createUserLoginRequest ();

        UserLoginResponse result = userService.registerUser (userLoginRequest);

        assertThat (result.getMessage ()).isEqualTo (REGISTER_SUCCESS);
    }

    @Test
    @DisplayName ("Register New User : User details should be saved in db")
    void registerUser_userDetailsSaveInDB_returnsSuccessResponse() {

        UserLoginRequest userLoginRequest = createUserLoginRequest ();

        UserLoginResponse result = userService.registerUser (userLoginRequest);

        Users savedUser = userRepository.findByUsername(USERNAME);
        assertThat (savedUser.getUsername ()).isEqualTo (USERNAME);
    }

    @Test
    @DisplayName ("Register Existing User : Check the user is already registered")
    void registerUser_checkUserAlreadyRegistered_returnsUserExistsResponse() {

        Users existingUser = Users.builder()
                .username(USERNAME)
                .firstname(FIRSTNAME)
                .lastname(LASTNAME)
                .password(PASSWORD).build();
        userRepository.save(existingUser);
        UserLoginRequest userLoginRequest = createUserLoginRequest ();

        UserLoginResponse result = userService.registerUser(userLoginRequest);

        assertThat(result.getMessage()).isEqualTo(USER_EXISTS);

    }

    @Test
    @DisplayName ("Register User : Check encrypted password is saved in db")
    void registerUser_encryptPassword_returnsResponse() {

        UserLoginRequest userLoginRequest = createUserLoginRequest ();

        UserLoginResponse result = userService.registerUser(userLoginRequest);
        Users savedUser = userRepository.findByUsername(USERNAME);

        assertThat(savedUser.getPassword ()).isNotEqualTo (PASSWORD);

    }

    @Test
    @DisplayName ("Login User : Returns success response for valid user ")
    void loginUser_validUser_returnsResponse() {

        UserLoginRequest userLoginRequest = createUserLoginRequest ();

        UserLoginResponse userRegister = userService.registerUser(userLoginRequest);

        UserLoginResponse result = userService.loginUser(userLoginRequest);

        assertThat (result.getMessage ()).isEqualTo (REGISTER_SUCCESS);
    }

    @Test
    @DisplayName ("Login User : Invalid user should throw UserNotFoundException")
    void loginUser_userNotInDB_returnsResponse() {

        UserLoginRequest userLoginRequest = createUserLoginRequest ();

        assertThrows(UserNotFoundException.class, () -> userService.loginUser(userLoginRequest));
    }

    @Test
    @DisplayName ("Login User : Invalid Credentials should throw UnauthorizedException")
    void loginUser_invalidCredentials_returnsResponse() {

        UserLoginRequest userLoginRequest = createUserLoginRequest ();
        UserLoginResponse userRegister = userService.registerUser(userLoginRequest);

        UserLoginRequest invalidRequest = UserLoginRequest.builder()
                .username(USERNAME)
                .firstName(FIRSTNAME)
                .lastName(LASTNAME)
                .password(PASSWORD_NEW).build();

        assertThrows(UnauthorizedException.class, () -> userService.loginUser(invalidRequest));
    }

    private UserLoginRequest  createUserLoginRequest(){

        return UserLoginRequest.builder ()
                .username (USERNAME)
                .firstName (FIRSTNAME)
                .lastName (LASTNAME)
                .password (PASSWORD).build ();
    }
}
