package com.sompo.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.sompo.demo.entity.User;
import com.sompo.demo.model.RegisterUserRequest;
import com.sompo.demo.model.UpdateUserRequest;
import com.sompo.demo.model.UserResponse;
import com.sompo.demo.repository.UserRepository;

import java.util.Objects;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validate(request);

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setLongname(request.getLongname());

        userRepository.save(user);
    }

    public UserResponse get(User user) {
        log.info("data user : {}", user.getLongname());
        return UserResponse.builder()
                .username(user.getUsername())
                .longname(user.getLongname())
                .build();
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request) {
        validationService.validate(request);

        log.info("REQUEST : {}", request);

        if (Objects.nonNull(request.getLongname())) {
            user.setLongname(request.getLongname());
        }

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(request.getPassword());
        }

        userRepository.save(user);

        log.info("USER : {}", user.getLongname());

        return UserResponse.builder()
                .longname(user.getLongname())
                .username(user.getUsername())
                .build();
    }

    @Transactional
    public void unreg(User user, String username) {
        User contact = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userRepository.delete(contact);
    }
}
