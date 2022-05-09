package com.intecon.GatewayServerApplication.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  
public class Controller {  
  
    @GetMapping("/auth")  
    public String response() {  
        return "NOT AUTH";  
    }  
}