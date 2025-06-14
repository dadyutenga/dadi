package com.example.dadi.controller;

import com.example.dadi.model.User;
import com.example.dadi.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final String adminKey;

    public AuthController(UserService userService, @Value("${app.admin.key}") String adminKey) {
        this.userService = userService;
        this.adminKey = adminKey;
    }

    @GetMapping("/select-role")
    public String selectRole() {
        return "auth/select-role";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               @RequestParam(value = "role", required = false) String role,
                               Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully!");
        }
        model.addAttribute("role", role != null ? role : "user");
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(@RequestParam(value = "role", required = false) String role, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("role", role != null ? role : "user");
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                             @RequestParam("role") String role,
                             @RequestParam(value = "adminKey", required = false) String adminKey,
                             Model model) {
        try {
            if ("admin".equalsIgnoreCase(role) && !this.adminKey.equals(adminKey)) {
                model.addAttribute("error", "Invalid admin key!");
                model.addAttribute("role", role);
                return "auth/register";
            }
            
            userService.registerUser(user, role.toUpperCase());
            return "redirect:/auth/login?registered&role=" + role.toLowerCase();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("role", role);
            return "auth/register";
        }
    }

    @GetMapping("/redirect")
    public String redirectBasedOnRole(Principal principal) {
        if (principal != null) {
            User user = (User) ((org.springframework.security.core.userdetails.UserDetails) 
                ((Authentication) principal).getPrincipal());
            
            if (user.getRole() == User.Role.ADMIN) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/user/dashboard";
            }
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/auth/login?logout";
    }
}
