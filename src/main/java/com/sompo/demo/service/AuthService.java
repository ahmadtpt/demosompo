package com.sompo.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sompo.demo.repository.UserRepository;
import com.sompo.demo.model.LoginUserRequest;
import com.sompo.demo.model.TokenResponse;
import com.sompo.demo.entity.User;

import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Username or password wrong, please check again"));

        if (request.getPassword().equals(user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpired(next30Days());
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpired())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Username or password wrong");
        }
    }

    private Long next30Days() {
        return System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 30);
    }

    @Transactional
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpired(null);

        userRepository.save(user);
    }
}
