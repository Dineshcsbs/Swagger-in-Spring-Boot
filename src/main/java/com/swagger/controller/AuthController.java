package com.swagger.controller;


import com.swagger.dto.ResponseDTO;
import com.swagger.dto.SignInRequestDTO;
import com.swagger.dto.SignUpRequestDTO;
import com.swagger.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseDTO signUp(@RequestBody final SignUpRequestDTO signUpRequestDTO) {
        return this.authService.createUser(signUpRequestDTO);
    }

//    @GetMapping("/user/{id}")
//    public ResponseDTO retrieveUser(@PathVariable final String id) {
//        return this.authService.retrieveUser(id);
//    }

    @PostMapping("/token")
    public ResponseDTO signIn(@RequestBody final SignInRequestDTO authRequest) {
        return this.authService.generateToken(authRequest);
    }

//    @PutMapping("/activate-user")
//    public ResponseDTO activateUser(String email) {
//        return this.authService.activateUser(email);
//    }
//
//    @PutMapping("/di-activate-user")
//    public ResponseDTO diActivateUser(String email) {
//        return this.authService.diActivateUser(email);
//    }

//    @PutMapping("/remove-user")
//    public ResponseDTO removeUser(String email) {
//        return this.authService.removeUser(email);
//    }
//
//    @GetMapping("/roles")
//    public ResponseDTO retrieveRoles() {
//        return this.authService.retrieveRoles();
//    }

    @GetMapping("/validate")
    public ResponseDTO validateToken(String token) {
        return this.authService.validateToken(token);
    }

//    @GetMapping("/account")
//    public ResponseDTO account(@RequestHeader("Authorization")String token){
//        return this.authService.account(token);
//    }

}
