package com.example.dadi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping({"/", "/welcome"})
    public String welcome() {
        return "welcome";
    }
}
