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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            UserDetails user = userRepository.findByEmail(request.getMail()).orElseThrow();
            String token = jwtService.getToken(user);
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
                User user = User.builder()
                        .name(request.getName())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .city(request.getCity())
                        .cell(request.getCell())
                        .email(request.getEmail())
                        .roleClient(ClientRole.USER)
                        .build();

                userRepository.save(user);
                return  ResponseEntity.ok(AuthResponse.builder().token(jwtService.getToken(user)).build());
            }
        } catch (Exception e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
