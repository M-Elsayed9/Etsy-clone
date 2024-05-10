package com.etsyclone.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.etsyclone.security.jwt.JwtGenerator.JWT_EXPIRATION;

@Controller
@RequestMapping("/auth")
public class UserAuthController {

    private final UserService userService;

    @Autowired
    public UserAuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "login-form";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        try {
            String token = userService.login(userDTO); // This should internally use JwtGenerator to create the token

            // Create cookie with JWT
            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true); // Make cookie inaccessible to client-side scripts
            jwtCookie.setPath("/"); // Cookie is available for all paths
            jwtCookie.setMaxAge((int) JWT_EXPIRATION / 1000); // Set cookie expiration
            response.addCookie(jwtCookie); // Add cookie to the response

            redirectAttributes.addFlashAttribute("success", "Login successful");
            return "redirect:/home"; // Redirect to home or another secure page
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Login failed: " + e.getMessage());
            return "redirect:/auth/login"; // Redirect back to the login page on failure
        }
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "register-form";
    }

    @PostMapping("/register")
    public String registerUser(UserDTO userDTO, RedirectAttributes redirectAttributes) {
        try {
            userService.customerRegistration(userDTO);
            redirectAttributes.addFlashAttribute("success", "Registration successful. Please login.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/auth/register";
        }
    }
}
