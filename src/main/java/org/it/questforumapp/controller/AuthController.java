package org.it.questforumapp.controller;


import lombok.RequiredArgsConstructor;
import org.it.questforumapp.model.User;
import org.it.questforumapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        handleLoginError(error, model);
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent() && !user.get().isVisible()) {
            return "redirect:/login?error=blocked";
        }

        return "redirect:/home";
    }

    @GetMapping("/register")
    public ModelAndView registerPage() {
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/register?error=exists";
        }

        createAndSaveUser(username, password);
        return "redirect:/login?success=true";
    }

    private void handleLoginError(String error, Model model) {
        if ("blocked".equals(error)) {
            model.addAttribute("errorMessage", "Twoje konto zostało zablokowane.");
        } else if ("true".equals(error)) {
            model.addAttribute("errorMessage", "Błędne dane logowania!");
        }
    }

    private void createAndSaveUser(String username, String password) {
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role("ROLE_USER")
                .isVisible(true)
                .build();
        userRepository.save(user);
    }
}
