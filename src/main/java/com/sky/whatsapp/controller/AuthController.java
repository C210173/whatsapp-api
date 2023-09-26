package com.sky.whatsapp.controller;

import com.sky.whatsapp.config.TokenProvider;
import com.sky.whatsapp.exception.UserException;
import com.sky.whatsapp.model.User;
import com.sky.whatsapp.repository.UserRepository;
import com.sky.whatsapp.request.LoginRequest;
import com.sky.whatsapp.response.AuthResponse;
import com.sky.whatsapp.service.CustomUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final CustomUserService customUserService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {

        User isUser = userRepository.findUserByEmail(user.getEmail());
        if (isUser!=null){
            throw new UserException("Email is used with another account "+ user.getEmail());
        }
        User createdUser = new User();
        createdUser.setEmail(user.getEmail());
        createdUser.setFullName(user.getFullName());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(createdUser.getEmail(), createdUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse(jwt, true);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req){
        Authentication authentication = authenticate(req.getEmail(), req.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse(jwt, true);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    private Authentication authenticate( String username, String password){
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        if (userDetails==null){
            throw new BadCredentialsException("invalid username");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("invalid password or username");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
