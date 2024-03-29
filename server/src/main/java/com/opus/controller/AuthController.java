package com.opus.controller;

import com.opus.converters.ClientDtoConverter;
import com.opus.dto.ClientDTO;
import com.opus.dto.request.ConfirmUserDTO;
import com.opus.dto.request.CreateUserDTO;
import com.opus.dto.request.JwtRequest;
import com.opus.dto.response.AuthResponse;
import com.opus.entity.Client;
import com.opus.entity.User;
import com.opus.repository.ClientRepository;
import com.opus.security.JwtHelper;
import com.opus.service.ClientService;
import com.opus.service.UserService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/")
    public ClientDTO getOpus() {
        return clientService.getClientById(1L);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody CreateUserDTO createUserDTO) throws MessagingException {
        userService.registerUser(1L, createUserDTO);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody JwtRequest request) {
        this.doAuthenticate(request.getEmail(), request.getPassword());
        User userDetails = (User) userDetailsService.loadUserByUsername(request.getEmail());
        if (userDetails == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        String token = this.helper.generateToken(userDetails);
        AuthResponse response = new AuthResponse(token, userDetails.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestBody ConfirmUserDTO confirmUserDTO) {
        userService.confirmUser(confirmUserDTO);
        return ResponseEntity.ok("Success");
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);

        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}
