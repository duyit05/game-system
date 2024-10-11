package com.rent.game.controller;

import com.rent.game.dto.LoginDTO;
import com.rent.game.dto.SignUpDTO;
import com.rent.game.dto.UserInfor;
import com.rent.game.model.Account;
import com.rent.game.repository.AccountRepository;
import com.rent.game.service.CustomUserDetailsService;
import com.rent.game.service.JwtService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @PostMapping("/token")
    public ResponseEntity<UserInfor> authenticateAndGetToken(@RequestBody LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(loginDto.getUsername());
            Cookie tokenCookie = new Cookie("token", token);
            tokenCookie.setMaxAge(3600);

            Account account = customUserDetailsService.getUserInfoByUsername(loginDto.getUsername());

            if (account != null) {

                UserInfor userInfor = new UserInfor();
                userInfor.setToken(token);
                userInfor.setId(account.getId());
                userInfor.setUsername(loginDto.getUsername());

                return new ResponseEntity<>(userInfor, HttpStatus.OK);
            } else {
                throw new UsernameNotFoundException("User not Found!!!");
            }

        } else {
            throw new UsernameNotFoundException("Invalid user!");
        }
    }

    @GetMapping("/username")
    public String getUsernameFromToken(Principal principal) {
        return principal.getName();
    }


    @PostMapping("/register")
    public String addNewUser(@RequestBody SignUpDTO signUpDto) {
        if (accountRepository.existsByMail(signUpDto.getMail())) {
            return "Email already exists";
        }
        if (accountRepository.existsByUsername(signUpDto.getUsername())) {
            return "Username already exists";
        }
        return customUserDetailsService.addUser(signUpDto);
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String mail) {
        return new ResponseEntity<String>(customUserDetailsService.forgotPassword(mail), HttpStatus.OK);
    }


    @PutMapping("/set-password")
    public ResponseEntity<String> setPassword(@RequestParam String mail, @RequestHeader String newPassword) {
        return new ResponseEntity<>(customUserDetailsService.setPassword(mail, newPassword), HttpStatus.OK);
    }
}
