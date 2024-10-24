package com.rojas.dev.XCampo.service.impl;

import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.service.Interface.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(User newUser) {
        return userRepository.save(newUser);
    }

    public Optional<User> showUserID(Long ID) {
        return userRepository.findById(ID);
    }

    public void deleteUserID(Long Id) {
        userRepository.deleteById(Id);
    }

    public User updateUser(Long Id, User postUser) {
        Optional<User> userExist = showUserID(Id);
        if(userExist.isPresent()) {
            User user = userExist.get();
            user.setName(postUser.getName());
            user.setCity(postUser.getCity());
            user.setCell(postUser.getCell());
            user.setEmail(postUser.getEmail());
            user.setPassword(postUser.getPassword());

            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not Found");
        }
    }

    public User findByIdUser(Long Id) {
        return userRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("User not Found"));
    }


}
