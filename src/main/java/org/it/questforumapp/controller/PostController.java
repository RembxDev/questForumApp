package org.it.questforumapp.controller;


import lombok.RequiredArgsConstructor;
import org.it.questforumapp.config.UserLoginListener;
import org.it.questforumapp.model.Post;
import org.it.questforumapp.model.User;
import org.it.questforumapp.service.AnswerService;
import org.it.questforumapp.service.PostService;
import org.it.questforumapp.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    @Value("${app.posts.perPage}")
    private int postsPerPage;

    private final PostService postService;
    private final UserService userService;
    private final AnswerService answerService;

    @GetMapping
    public String listPosts(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            Model model,
            Principal principal) {
        User user = getUserFromPrincipal(principal);
        model.addAttribute("user", user);

        List<Post> posts = fetchPosts(search, page, model);
        addPostAttributes(model, posts, search, page);

        return "posts/list";
    }

    @GetMapping("/{postId}")
    public String viewPost(@PathVariable Integer postId, Model model) {
        Post post = postService.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Nie odnaleziono posta."));
        model.addAttribute("post", post);
        model.addAttribute("answers", post.getAnswers());
        return "posts/view";
    }

    @GetMapping("/add")
    public String showAddPostForm(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", principal.getName());
        model.addAttribute("post", new Post());
        return "posts/add";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute Post post, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(principal.getName());
        initializePost(post, user);
        postService.addPost(post);
        return "redirect:/posts";
    }

    private User getUserFromPrincipal(Principal principal) {
        return principal != null ? userService.findByUsername(principal.getName()) : null;
    }

    private List<Post> fetchPosts(String search, int page, Model model) {
        int totalPages;
        List<Post> posts;

        if (search != null && !search.isEmpty()) {
            totalPages = postService.getTotalPagesBySearchString(search);
            posts = totalPages > 0 ? postService.getAllPostsBySearchString(search, page) : new ArrayList<>();
        } else {
            totalPages = postService.getTotalPagesByDateCreated();
            posts = totalPages > 0 ? postService.getAllPostsByDateCreated(page) : new ArrayList<>();
        }

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        return posts;
    }

    private void addPostAttributes(Model model, List<Post> posts, String search, int page) {
        model.addAttribute("posts", posts);
        model.addAttribute("popularPosts", postService.getRecentPosts());
        model.addAttribute("totalQuestions", postService.getTotalPostCount());
        model.addAttribute("totalAnswers", answerService.getTotalAnswerCount());
        model.addAttribute("loggedUsers", UserLoginListener.getLoggedInUsers());

        if (posts.isEmpty()) {
            model.addAttribute("noResultsMessage", "Brak postów spełniających kryteria.");
        }
    }

    private void initializePost(Post post, User user) {
        post.setAuthor(user);
        post.setDateCreated(LocalDateTime.now());
        post.setAnswerCount(0);
    }
}
