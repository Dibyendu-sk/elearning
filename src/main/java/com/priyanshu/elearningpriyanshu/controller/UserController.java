package com.priyanshu.elearningpriyanshu.controller;

import com.priyanshu.elearningpriyanshu.entity.UserEntity;
import com.priyanshu.elearningpriyanshu.model.LoggedinUserDtls;
import com.priyanshu.elearningpriyanshu.model.Response;
import com.priyanshu.elearningpriyanshu.service.Services;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final Services services;

    public UserController(Services services) {
        this.services = services;
    }
    @PostMapping("/sign-up")
    public ResponseEntity<Response<String>> signUp(@RequestBody UserEntity userEntity){
        Boolean isCreated = services.signUp(userEntity);
        if (isCreated){
            return ResponseEntity.ok(new Response<>(HttpStatus.CREATED.value(), "User successfully created."));
        }
        else {
            return ResponseEntity.ok(new Response<>(HttpStatus.EXPECTATION_FAILED.value(), "error while signing up"));
        }
    }
    @PostMapping("/log-in")
    @Operation(summary = "log in", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<Response<LoggedinUserDtls>> login(){
        LoggedinUserDtls loggedinUserDtls = services.login();
        return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(),loggedinUserDtls));
    }
}
