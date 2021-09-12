package com.mateuszjanczak.barrelsbeer.configuration.security.service;

import com.mateuszjanczak.barrelsbeer.configuration.security.JwtProvider;
import com.mateuszjanczak.barrelsbeer.configuration.security.dto.*;
import com.mateuszjanczak.barrelsbeer.configuration.security.entity.User;
import com.mateuszjanczak.barrelsbeer.configuration.security.entity.UserRefreshToken;
import com.mateuszjanczak.barrelsbeer.configuration.security.exception.UserAlreadyExistsException;
import com.mateuszjanczak.barrelsbeer.configuration.security.exception.UserNotFoundException;
import com.mateuszjanczak.barrelsbeer.configuration.security.exception.WrongPasswordException;
import com.mateuszjanczak.barrelsbeer.configuration.security.mapper.UserMapper;
import com.mateuszjanczak.barrelsbeer.configuration.security.repository.UserRefreshTokenRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(UserService userService, UserRefreshTokenRepository userRefreshTokenRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userService = userService;
        this.userRefreshTokenRepository = userRefreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    private LoginResponse doLogin(User user) {
        String token = jwtProvider.createToken(user.getUsername());
        String refreshToken = createRefreshToken(user);
        return new LoginResponse(token, refreshToken);
    }

    private String createRefreshToken(User user) {
        String refreshToken = RandomStringUtils.randomAlphanumeric(128);
        userRefreshTokenRepository.save(new UserRefreshToken(refreshToken, user));
        return refreshToken;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        return userService.findByUsername(loginRequest.getUsername())
                .map(user -> {
                    if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                        return doLogin(user);
                    } else {
                        throw new WrongPasswordException();
                    }
                }).orElseThrow(UserNotFoundException::new);
    }

    public UserResponse register(RegisterRequest registerRequest) {

        Optional<User> existingUser = userService.findByUsername(registerRequest.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException();
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User persistedUser = userService.save(user);


        return UserMapper.userToResponse(persistedUser);
    }

    public User getLoggedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Optional<TokenResponse> refreshToken(String refreshToken) {
        return userRefreshTokenRepository.findByToken(refreshToken)
                .map(userRefreshToken -> new TokenResponse(jwtProvider.createToken(userRefreshToken.getUser().getUsername())));
    }

    public void logout(String refreshToken) {
        userRefreshTokenRepository.findByToken(refreshToken)
                .ifPresent(userRefreshTokenRepository::delete);
    }
}