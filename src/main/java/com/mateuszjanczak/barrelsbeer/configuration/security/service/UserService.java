package com.mateuszjanczak.barrelsbeer.configuration.security.service;

import com.mateuszjanczak.barrelsbeer.configuration.security.entity.User;
import com.mateuszjanczak.barrelsbeer.configuration.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String username) {
        return findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
