package com.bnpp.kata.onlinebookstore.controller;

import com.bnpp.kata.onlinebookstore.exception.UnauthorizedException;
import com.bnpp.kata.onlinebookstore.service.UserService;
import com.bnpp.kata.onlinebookstore.models.UserLoginRequest;
import com.bnpp.kata.onlinebookstore.models.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.bnpp.kata.onlinebookstore.constants.Constants.INVALID_CREDENTIALS;

@RestController
@RequiredArgsConstructor
@RequestMapping("${onlinebookstore.usercontroller.path}")
public class UserController {

    private final UserService userService;

    @PostMapping("${onlinebookstore.endpoint.register}")
    public ResponseEntity<UserLoginResponse> register(@RequestBody UserLoginRequest registerRequest) {

        UserLoginResponse response = userService.registerUser(registerRequest);

        return ResponseEntity.status(response.getValidResponse ()? HttpStatus.CREATED : HttpStatus.NOT_FOUND)
                .body(response);

    }

    @PostMapping("${onlinebookstore.endpoint.login}")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest registerRequest) {

        if(checkRequestIsNotEmpty (registerRequest)){
            return ResponseEntity.ok (userService.loginUser (registerRequest));
        }
        throw new UnauthorizedException (INVALID_CREDENTIALS);
    }

    private static boolean checkRequestIsNotEmpty (UserLoginRequest registerRequest) {
        return StringUtils.isNotEmpty (registerRequest.getUsername ()) && StringUtils.isNotEmpty (registerRequest.getPassword ());
    }
}
