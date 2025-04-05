package org.it.questforumapp.controller;


import lombok.RequiredArgsConstructor;
import org.it.questforumapp.service.AnswerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/add/{postId}")
    public String addAnswer(
            @PathVariable Integer postId,
            @RequestParam String content,
            Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        if (isContentInvalid(content)) {
            return "redirect:/posts/" + postId + "?error=contentRequired";
        }

        logUserInfo(principal);
        answerService.addAnswer(postId, content, principal);

        return "redirect:/posts/" + postId;
    }

    private boolean isContentInvalid(String content) {
        return content == null || content.trim().isEmpty();
    }

    private void logUserInfo(Principal principal) {
        System.out.println("Zalogowany użytkownik: " + principal.getName());
    }
}
