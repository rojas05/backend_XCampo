package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.exception.EntityNotFoundException;
import com.rojas.dev.XCampo.repository.UserRepository;
import com.rojas.dev.XCampo.service.Interface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addUser(User newUser) {
        Long Id = newUser.getUser_id();
        if(userRepository.existsById(Id)) {
            throw new IllegalArgumentException("User existed with ID: " + Id);
        }

        return userRepository.save(newUser);
    }

    @Override
    public void deleteUserID(Long Id) {
        existsUserId(Id);
        userRepository.deleteById(Id);
    }

    @Override
    public User updateUser(Long Id, User postUser) {
        existsUserId(Id);

        postUser.setName(postUser.getName());
        postUser.setCity(postUser.getCity());
        postUser.setCell(postUser.getCell());
        postUser.setEmail(postUser.getEmail());
        postUser.setPassword(postUser.getPassword());

        return userRepository.save(postUser);
    }

    @Override
    public Optional<User> findByIdUser(Long Id) {
        return userRepository.findById(Id);
    }

    @Override
    public void existsUserId(Long Id) {
        if (!userRepository.existsById(Id)) {
            throw new EntityNotFoundException("User not found or does not exist with the ID: " + Id);
        }
    }

    @Override
    public List<User> listAllUser() {
        return userRepository.findAll();
    }

    @Override
    public ResponseEntity<?> getNFSidByIdUser(Long id_user) {
        Optional<String> result = userRepository.getNFSidByIdUser(id_user);
        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user with id " + id_user + " not found.");

        }

        return ResponseEntity.ok().body(result);
    }

}
