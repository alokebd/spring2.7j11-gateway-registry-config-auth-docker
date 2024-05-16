package com.adas.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.adas.authservice.model.User;
import com.adas.authservice.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findById(int userId) {
        return userRepository.findById(userId);
    }
}
