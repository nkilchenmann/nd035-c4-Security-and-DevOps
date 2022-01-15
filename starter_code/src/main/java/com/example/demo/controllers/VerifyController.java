package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerifyController {
    @GetMapping("/verify")
    public String verify() {
        return "ecommerce application";
    }
}
