package com.flab.authuser.auth.jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/secure/tokenTest")
    public String jwtResponseTest(){
        return "jwt verification passed";
    }
}
