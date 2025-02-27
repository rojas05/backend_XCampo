package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.Auth.AuthResponse;
import com.rojas.dev.XCampo.Auth.LoginRequest;
import com.rojas.dev.XCampo.Auth.RegisterRequest;
import com.rojas.dev.XCampo.entity.User;
import com.rojas.dev.XCampo.enumClass.ClientRole;
import com.rojas.dev.XCampo.repository.UserRepository;
import com.rojas.dev.XCampo.service.Interface.AuthService;
import com.rojas.dev.XCampo.service.Interface.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class AuthServiceImp implements AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<?> login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getMail(),request.getPassword()));
            User user = userRepository.findByEmail(request.getMail()).orElseThrow();
            AuthResponse token = jwtService.getToken(user, user.getUser_id());
            return ResponseEntity.ok(token);
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Error occurred while auth the user: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> register(RegisterRequest request) {
        try {
            Optional<User> result = userRepository.findByEmail(request.getEmail());
            if (result.isPresent()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("user mail exist " + result.get().getEmail());
            }else {
                System.out.println(passwordEncoder.encode(request.getPassword()));
                User user = User.builder()
                        .name(request.getName())
                        .department(request.getDepartment())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .city(request.getCity())
                        .cell(request.getCell())
                        .email(request.getEmail())
                        .roleClient(ClientRole.USER)
                        .build();

                userRepository.save(user);
                return  ResponseEntity.ok(jwtService.getToken(user,user.getUser_id()));
            }
        } catch (Exception e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
