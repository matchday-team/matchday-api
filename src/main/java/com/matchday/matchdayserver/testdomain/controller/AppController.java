package com.matchday.matchdayserver.testdomain.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    @GetMapping("/")
    public String hello() {
        return 1234;
    }
}
