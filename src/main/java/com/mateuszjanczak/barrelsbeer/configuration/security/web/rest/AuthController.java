package com.mateuszjanczak.barrelsbeer.configuration.security.web.rest;

import com.mateuszjanczak.barrelsbeer.configuration.security.dto.*;
import com.mateuszjanczak.barrelsbeer.configuration.security.entity.User;
import com.mateuszjanczak.barrelsbeer.configuration.security.exception.InvalidRefreshTokenException;
import com.mateuszjanczak.barrelsbeer.configuration.security.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class AuthController {

    public static final String PATH_POST_LOGIN = "/api/auth/login";
    public static final String PATH_POST_SIGN_UP = "/api/auth/register";
    public static final String PATH_POST_REFRESH_TOKEN = "/api/auth/token/refresh";
    public static final String PATH_DELETE_LOGOUT = "/api/auth/logout";
    private static final String PATH_GET_ME = "/api/auth/me";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(PATH_POST_LOGIN)
    public ResponseEntity<LoginResponse> userPostLogin(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping(PATH_POST_SIGN_UP)
    public ResponseEntity<UserResponse> userPostRegister(@Valid @RequestBody RegisterRequest registerRequest) {
        UserResponse userResponse = authService.register(registerRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping(PATH_POST_REFRESH_TOKEN)
    public @ResponseBody
    TokenResponse tokenPostRefresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest.getRefreshToken()).orElseThrow(InvalidRefreshTokenException::new);
    }

    @DeleteMapping(PATH_DELETE_LOGOUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void tokenDeleteLogout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        authService.logout(refreshTokenRequest.getRefreshToken());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = PATH_GET_ME, method = RequestMethod.GET)
    public @ResponseBody
    User tokenGetMe() {
        return authService.getLoggedUser();
    }
}